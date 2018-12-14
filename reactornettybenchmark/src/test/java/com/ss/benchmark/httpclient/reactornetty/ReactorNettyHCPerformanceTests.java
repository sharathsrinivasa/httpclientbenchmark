package com.ss.benchmark.httpclient.reactornetty;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;
import com.ss.benchmark.httpclient.common.BenchmarkCommon;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.testng.Assert.assertEquals;

/**
 * @author sharath.srinivasa
 */
@Test(groups = "performance")
public class ReactorNettyHCPerformanceTests extends BenchmarkCommon {

    private static final Logger logger = LoggerFactory.getLogger(ReactorNettyHCPerformanceTests.class);

    HttpClient reactorNettyClient;
    AtomicInteger count = new AtomicInteger(0);

    private Set<CountDownLatch> latches = new HashSet<>();
    private CountDownLatch testMultiThreadedNonBlockingGETLatch = null;
    private Lock lock = new ReentrantLock();

    @BeforeTest
    public void initializeTest() {
        setupMetrics();
        reactorNettyClient = setupReactorNettyHttpClient(this.getBaseUrl());
    }

    // TODO : use atomic interger rather than int in async execute
    @AfterTest
    public void finalizeTest(){
        for (CountDownLatch latcher : latches) {
            try {
                latcher.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        tearDownMetrics();
    }

    @Test(invocationCount = MAX_CONNECTION_POOL_SIZE, threadPoolSize = MAX_CONNECTION_POOL_SIZE, priority = 0)
    public void warmupBlocking() {
        //using blocking requests here
        Timer timer = getTimingTimer(this.getClass(), "warmupBlocking");
        Counter errors = getErrorCounter(this.getClass(), "warmupBlocking");
        String uuid = UUID.randomUUID().toString();

        HttpClient.RequestSender requestSender = reactorNettyClient
                .request(HttpMethod.GET)
                .uri(echoURL(uuid)); // random endpoint

        Timer.Context ctx = timer.time();
        try {
            String result = executeSync(requestSender);
//            assertEquals(result, BenchmarkCommon.RANDOM_ECHO_RESPONSE);
            ctx.stop();
        } catch(Exception ex) {
            ctx.stop();
            errors.inc();
            ex.printStackTrace();
        }
    }

    @Test(priority = 1)
    public void testVanillaBlockingGET() throws Exception {
        logger.info("Start testVanillaBlockingGET");
        CountDownLatch latch = new CountDownLatch(1);

        Mono<String> responseVerifier = reactorNettyClient
                .request(HttpMethod.GET)
                .uri(BenchmarkCommon.TEST_ENDPOINT)
                .responseContent()
                .aggregate()
                .map(byteBuf -> byteBuf.toString(StandardCharsets.UTF_8));

        StepVerifier
                .create(responseVerifier)
                .assertNext(s -> assertEquals(s, RANDOM_ECHO_RESPONSE ))
                .verifyComplete();
        logger.info("Completed testVanillaBlockingGET");
    }

    @Test(priority = 2)
    public void testSimpleShorttoShortPost() throws Exception {

        Flux<String> responseVerifier = reactorNettyClient
                .headers(entries -> entries.add("Content-Type", "application/json" ))
                .post()
                .uri(ECHO_DELAY_POST_SHORT_URL)
                .send(ByteBufFlux.fromString(Flux.just(SHORT_JSON)))
                .response((res, responseBody) -> {
                    if (res.status().code() != 200) {
                        Mono.error(new IllegalStateException("Unexpected response code : " + res.status().code()));
                    }
                    return responseBody;
                })
                .map(byteBuf -> byteBuf.toString(StandardCharsets.UTF_8));

        StepVerifier
                .create(responseVerifier)
                .assertNext(s -> assertEquals(s, MICRO_JSON ))
                .verifyComplete();
    }

    @Test(priority = 3)
    public void testBlockingGET() {
        Timer timer = getTimingTimer(this.getClass(), "testBlockingGET");
        Counter errors = getErrorCounter(this.getClass(), "testBlockingGET");
        for (int i = 0; i < EXECUTIONS; i++){
            String uuid = UUID.randomUUID().toString();

            HttpClient.RequestSender requestSender = reactorNettyClient
                    .request(HttpMethod.GET)
                    .uri(echoURL(uuid));

            Timer.Context ctx = timer.time();
            try {
                String result = executeSync(requestSender);
//                assertEquals(result, RANDOM_ECHO_RESPONSE);
                ctx.stop();
            } catch(Exception ex) {
                ctx.stop();
                errors.inc();
                ex.printStackTrace();
            }
        }
    }

    @Test(priority = 4)
    public void testNonBlockingGET() {
        logger.info("testNonBlockingGET start");
        CountDownLatch latcher = new CountDownLatch(EXECUTIONS);
        Timer timer = getTimingTimer(this.getClass(), "testNonBlockingGET");
        Counter errors = getErrorCounter(this.getClass(), "testNonBlockingGET");
        for (int i = 0; i < EXECUTIONS; i++) {
            String uuid = UUID.randomUUID().toString();

            HttpClient.RequestSender requestSender = reactorNettyClient
                    .request(HttpMethod.GET)
                    .uri(echoURL(uuid));
            executeAsync(requestSender, timer, errors, latcher, i)
                    .subscribe(s -> {});
        }
        try {
            latcher.await();
        } catch (InterruptedException e) {
            logger.error("hey, don't interrupt me!");
            e.printStackTrace();
        }
        logger.info("testNonBlockingGET end");
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 100, priority = 5)
    public void testMultiThreadedBlockingGET() {
        Timer timer = getTimingTimer(this.getClass(), "testMultiThreadedBlockingGET");
        Counter errors = getErrorCounter(this.getClass(), "testMultiThreadedBlockingGET");
        String uuid = UUID.randomUUID().toString();
        HttpClient.RequestSender requestSender = reactorNettyClient
                .request(HttpMethod.GET)
                .uri(echoURL(uuid));
        Timer.Context ctx = timer.time();
        try {
            executeSync(requestSender);
            ctx.stop();
        } catch (Exception e) {
            ctx.stop();
            errors.inc();
            e.printStackTrace();
        }
    }

    //NOTE: run this one last - don't yet have a good way to make sure it does not bleed into the next benchmark case
    @Test(invocationCount = EXECUTIONS, threadPoolSize = 100, priority = 99)
    public void testMultiThreadedNonBlockingGET() {
        lock.lock();
        if (testMultiThreadedNonBlockingGETLatch == null){
            testMultiThreadedNonBlockingGETLatch = new CountDownLatch(EXECUTIONS);
            latches.add(testMultiThreadedNonBlockingGETLatch);
        }
        lock.unlock();
        Timer timer = getTimingTimer(this.getClass(), "testMultiThreadedNonBlockingGET");
        Counter errors = getErrorCounter(this.getClass(), "testMultiThreadedNonBlockingGET");
        String uuid = UUID.randomUUID().toString();

        HttpClient.RequestSender requestSender = reactorNettyClient
                .request(HttpMethod.GET)
                .uri(echoURL(uuid));

        executeAsync(
                requestSender,
                timer,
                errors,
                testMultiThreadedNonBlockingGETLatch,
                count.getAndIncrement()
        ).subscribe(message -> {});
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 100, priority = 6)
    public void testMultiThreadedBlockingShortShortPOST() {
        Timer timer = getTimingTimer(this.getClass(), "testMultiThreadedBlockingShortShortPOST");
        Counter errors = getErrorCounter(this.getClass(), "testMultiThreadedBlockingShortShortPOST");
        HttpClient.ResponseReceiver<?> requestSender = reactorNettyClient
                .post()
                .uri(ECHO_DELAY_POST_SHORT_URL)
                .send(ByteBufFlux.fromString(Mono.just(SHORT_JSON)));
        Timer.Context ctx = timer.time();
        try {
            executeSync(requestSender);
            ctx.stop();
        } catch (Exception e) {
            ctx.stop();
            errors.inc();
            e.printStackTrace();
        }
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 100, priority = 7)
    public void testMultiThreadedBlockingLongLongPOST() {
        Timer timer = getTimingTimer(this.getClass(), "testMultiThreadedBlockingLongLongPOST");
        Counter errors = getErrorCounter(this.getClass(), "testMultiThreadedBlockingLongLongPOST");

        HttpClient.ResponseReceiver<?> requestSender = reactorNettyClient
                .post()
                .uri(ECHO_DELAY_POST_LONG_URL)
                .send(ByteBufFlux.fromString(Mono.just(LONG_JSON)));
        Timer.Context ctx = timer.time();
        try {
            executeSync(requestSender);
            ctx.stop();
        } catch (Exception e) {
            ctx.stop();
            errors.inc();
            e.printStackTrace();
        }
    }

    @Test(priority = 8)
    public void testNonBlockingShortShortPOST() {
        CountDownLatch latcher = new CountDownLatch(EXECUTIONS);
        Timer timer = getTimingTimer(this.getClass(), "testNonBlockingShortShortPOST");
        Counter errors = getErrorCounter(this.getClass(), "testNonBlockingShortShortPOST");
        for (int i = 0; i < EXECUTIONS; i++){
            HttpClient.ResponseReceiver<?> requestSender = reactorNettyClient
                    .post()
                    .uri(ECHO_DELAY_POST_SHORT_URL)
                    .send(ByteBufFlux.fromString(Flux.just(SHORT_JSON)));
            executeAsync(requestSender, timer, errors, latcher, i).subscribe();
        }
        try {
            latcher.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 9)
    public void testNonBlockingShortLongPOST() {
        CountDownLatch latcher = new CountDownLatch(EXECUTIONS);
        Timer timer = getTimingTimer(this.getClass(), "testNonBlockingShortLongPOST");
        Counter errors = getErrorCounter(this.getClass(), "testNonBlockingShortLongPOST");
        for (int i = 0; i < EXECUTIONS; i++){
            HttpClient.ResponseReceiver<?> requestSender = reactorNettyClient
                    .post()
                    .uri(ECHO_DELAY_POST_LONG_URL)
                    .send(ByteBufFlux.fromString(Flux.just(SHORT_JSON)));
            executeAsync(requestSender, timer, errors, latcher, i).subscribe();
        }
        try {
            latcher.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 10)
    public void testNonBlockingLongShortPOST() {
        CountDownLatch latcher = new CountDownLatch(EXECUTIONS);
        Timer timer = getTimingTimer(this.getClass(), "testNonBlockingLongShortPOST");
        Counter errors = getErrorCounter(this.getClass(), "testNonBlockingLongShortPOST");
        for (int i = 0; i < EXECUTIONS; i++){
            HttpClient.ResponseReceiver<?> requestSender = reactorNettyClient
                    .post()
                    .uri(ECHO_DELAY_POST_SHORT_URL)
                    .send(ByteBufFlux.fromString(Flux.just(LONG_JSON)));
            executeAsync(requestSender, timer, errors, latcher, i).subscribe();
        }
        try {
            latcher.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 11)
    public void testNonBlockingLongLongPOST() {
        CountDownLatch latcher = new CountDownLatch(EXECUTIONS);
        Timer timer = getTimingTimer(this.getClass(), "testNonBlockingLongLongPOST");
        Counter errors = getErrorCounter(this.getClass(), "testNonBlockingLongLongPOST");
        for (int i = 0; i < EXECUTIONS; i++){
            HttpClient.ResponseReceiver<?> requestSender = reactorNettyClient
                    .post()
                    .uri(ECHO_DELAY_POST_LONG_URL)
                    .send(ByteBufFlux.fromString(Mono.just(LONG_JSON)));
            executeAsync(requestSender, timer, errors, latcher, i).subscribe();
        }
        try {
            latcher.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String executeSync(HttpClient.ResponseReceiver<?> request) {
        return request
                .responseSingle((res, body) -> {
                    if (res.status().code() != 200) {
                        Mono.error(new IllegalStateException("Unexpected response code : " + res.status().code()));
                    }
                    return body;
                })
                .map(byteBuf -> byteBuf.toString(StandardCharsets.UTF_8))
                .block();
    }

    public Mono<String> executeAsync(
            final HttpClient.ResponseReceiver<?> request,
            final Timer timer,
            final Counter errors,
            final CountDownLatch latch,
            final int counter
    ) {

        final AtomicReference<Timer.Context> ctx = new AtomicReference<>();

        return request
                .responseSingle((res, body) -> {
                    if (res.status().code() != 200) {
                        Mono.error(new IllegalStateException("Unexpected response code : " + res.status().code()));
                    }
                    return body;
                })
                .map(byteBuf -> byteBuf.toString(StandardCharsets.UTF_8))
                .doOnSubscribe(subscription -> ctx.set(timer.time()))
                .doOnTerminate(() -> {
                    ctx.get().stop();
                    if (latch != null) latch.countDown();
                })
                .doOnError(throwable -> {
                    errors.inc();
                    logger.error("Failed : {}", counter);
                    throwable.printStackTrace();
                });
    }

    // accepts a baseUrl as input and returns an instance of HttpClient
    private HttpClient setupReactorNettyHttpClient(String baseUrl) {
        return HttpClient
                .create(ConnectionProvider.fixed("benchmark", MAX_CONNECTION_POOL_SIZE, CONNECTION_REQUEST_TIMEOUT))
                .baseUrl(baseUrl)
                .tcpConfiguration(tcpClient ->
                        tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT)
                                .doOnConnected( con -> con.addHandlerLast(new ReadTimeoutHandler(READ_TIMEOUT, TimeUnit.MILLISECONDS)))
                );
    }
}

