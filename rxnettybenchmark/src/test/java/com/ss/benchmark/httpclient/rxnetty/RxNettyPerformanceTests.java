package com.ss.benchmark.httpclient.rxnetty;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Timer;
import com.ss.benchmark.httpclient.common.BenchmarkCommon;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelOption;
import io.reactivex.netty.client.Host;
import io.reactivex.netty.client.pool.PoolConfig;
import io.reactivex.netty.client.pool.SingleHostPoolingProviderFactory;
import io.reactivex.netty.protocol.http.client.HttpClient;
import io.reactivex.netty.protocol.http.client.HttpClientRequest;
import io.reactivex.netty.protocol.http.client.HttpClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.ss.benchmark.httpclient.common.Payloads;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author sharath.srinivasa
 */
@Test(groups = "performance")
public class RxNettyPerformanceTests extends BenchmarkCommon {
    private static final Logger logger = LoggerFactory.getLogger(RxNettyPerformanceTests.class);

    //Bounded connection pool
    PoolConfig poolConfig = null;
    HttpClient<ByteBuf, ByteBuf> client = null;

    AtomicInteger count = new AtomicInteger(0);
    CountDownLatch testMultiThreadedNonBlockingGETLatch = null;
    private Set<CountDownLatch> latches = new HashSet<>();
    private Lock lock = new ReentrantLock();

    @BeforeTest
    public void initializeTest() {
        this.setupMetrics();

        poolConfig = new PoolConfig().maxConnections(MAX_CONNECTION_POOL_SIZE).maxIdleTimeoutMillis(CONNECTION_TTL);
        client = HttpClient
                .newClient(SingleHostPoolingProviderFactory.<ByteBuf, ByteBuf>create(poolConfig),
                        Observable.just(new Host(new InetSocketAddress("localhost",8080)))).readTimeOut(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .channelOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT);

        TestSubscriber<HttpClientResponse<ByteBuf>> testSubscriber = new TestSubscriber<>();
        client.createGet(ECHO_DELAY_SETUP_FULL_URL).subscribe(testSubscriber);
        testSubscriber.awaitTerminalEvent(10, TimeUnit.SECONDS);
        for (Throwable t : testSubscriber.getOnErrorEvents()) {
            System.out.println("uh-oh: ");
            t.printStackTrace();
        }
        assertTrue(testSubscriber.getOnErrorEvents().isEmpty());
        assertEquals(testSubscriber.getOnNextEvents().size(), 1);
        HttpClientResponse<ByteBuf> response = testSubscriber.getOnNextEvents().get(0);
        assertEquals(response.getStatus().code(), 200);
    }

    @AfterTest
    public void finalizeTest(){
        for (CountDownLatch latcher : latches) {
                try {
                    latcher.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        this.tearDownMetrics();
    }

    public StringBuffer executeBlocking2(HttpClientRequest<ByteBuf, ByteBuf> request){
        final StringBuffer content = new StringBuffer();
        return request
                .flatMap(response ->{
                    if (response.getStatus().code() != 200) {
                        throw new IllegalStateException("Unexpected response code: " + response.getStatus().code());
                    }
                    return response.getContent();
                })
                .map(buffer -> {
                    byte[] bytes = new byte[buffer.readableBytes()];
                    buffer.readBytes(bytes);
                    buffer.release();
                    return new String(bytes);
                })
                .collect(() -> {return content;}, (StringBuffer sb, String s) -> {sb.append(s);})
                .toBlocking()
                .first();
    }

    public StringBuffer executeBlockingObservable(Observable<HttpClientResponse<ByteBuf>> observable){
        final StringBuffer content = new StringBuffer();
        return observable
                .flatMap(response ->{
                    if (response.getStatus().code() != 200) {
                        throw new IllegalStateException("Unexpected response code: " + response.getStatus().code());
                    }
                    return response.getContent();
                })
                .map(buffer -> {
                    byte[] bytes = new byte[buffer.readableBytes()];
                    buffer.readBytes(bytes);
                    buffer.release();
                    return new String(bytes);
                })
                .collect(() -> {return content;}, (StringBuffer sb, String s) -> {sb.append(s);})
                .toBlocking()
                .first();
    }

    public Observable<String> executeAsync(Observable<HttpClientResponse<ByteBuf>> request, Timer timer, Counter errors, final CountDownLatch latch, final int counter){
        final AtomicReference<Timer.Context> ctx = new AtomicReference<>();
        return request
                .flatMap(response -> {
                    if (response.getStatus().code() != 200) {
                        throw new IllegalStateException("Unexpected response code: " + response.getStatus().code());
                    }
                    return response.getContent();
                })
                .map(buffer -> {
                    byte[] bytes = new byte[buffer.readableBytes()];
                    buffer.readBytes(bytes);
                    buffer.release();
                    return new String(bytes);
                })
                .doOnSubscribe(() -> ctx.set(timer.time()))
                .doOnTerminate(() -> {
                    ctx.get().stop();
                    if (latch != null) latch.countDown();
                })
                .doOnError(throwable -> {
                    errors.inc();
                    System.err.println("failed: " + counter);
                    throwable.printStackTrace();
                }).onErrorReturn(error -> null);//do not propagate errors up to the subscriber...we don't plan to implement onError
                                               // there for our tests and we don't want OnErrorNotImplementedException leaking out
                                               // and stopping the tests. See http://reactivex.io/RxJava/javadoc/rx/exceptions/Exceptions.html#throwIfFatal(java.lang.Throwable)
    }

    @Test(invocationCount = MAX_CONNECTION_POOL_SIZE, threadPoolSize = MAX_CONNECTION_POOL_SIZE, priority = 0)
    public void warmupBlocking() {
        //using blocking requests here
        Timer timer = getTimingTimer(this.getClass(), "warmupBlocking");
        Counter errors = getErrorCounter(this.getClass(), "warmupBlocking");
        String uuid = UUID.randomUUID().toString();
        HttpClientRequest<ByteBuf, ByteBuf> request = client.createGet(echoURL(uuid));
        Timer.Context ctx = timer.time();
        try {
            StringBuffer sb = executeBlocking2(request);
            logger.info(sb.toString());
            ctx.stop();
        } catch (Exception e) {
            ctx.stop();
            errors.inc();
            e.printStackTrace();
        }
    }

    private StringBuffer executeSync() {

        return null;
    }


    @Test(priority = 1)
    public void testVanillaBlockingGET() throws Exception {
        TestSubscriber<HttpClientResponse<ByteBuf>> testsubscriber = new TestSubscriber<>();
        client.createGet(echoURL("simpletest")).subscribe(testsubscriber);
        testsubscriber.awaitTerminalEvent();
        testsubscriber.assertNoErrors();
        testsubscriber.assertCompleted();
        assertEquals(testsubscriber.getOnNextEvents().size(), 1);
        HttpClientResponse<ByteBuf> response = testsubscriber.getOnNextEvents().get(0);
        assertEquals(response.getStatus().code(), 200);
        response.getContent().map(buf -> {byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            return new String(bytes);}).doOnNext(output -> assertTrue(output.contains("{"))).toBlocking().subscribe(output -> System.out.print(output));
    }

    @Test(priority =2)
    public void testSimpleShorttoShortPost() throws Exception{
        TestSubscriber<HttpClientResponse<ByteBuf>> testsubscriber = new TestSubscriber<>();
        client.createPost(ECHO_DELAY_POST_SHORT_URL).addHeader("Content-Type", "application/json")
                .writeStringContent(Observable.just(Payloads.SHORT_JSON))
                .subscribe(testsubscriber);
        testsubscriber.awaitTerminalEvent();
        testsubscriber.assertNoErrors();
        testsubscriber.assertCompleted();
        assertEquals(testsubscriber.getOnNextEvents().size(), 1);
        HttpClientResponse<ByteBuf> response = testsubscriber.getOnNextEvents().get(0);
        assertEquals(response.getStatus().code(), 200);
        response.getContent().map(buf -> {byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            return new String(bytes);}).doOnNext(output -> assertTrue(output.contains("{"))).toBlocking().subscribe(output -> System.out.print(output));
    }

    @Test(priority =3)
    public void testBlockingGET(){
        Timer timer = getTimingTimer(this.getClass(), "testBlockingGET");
        Counter errors = getErrorCounter(this.getClass(), "testBlockingGET");
        for (int i = 0; i < EXECUTIONS; i++){
            String uuid = UUID.randomUUID().toString();
            HttpClientRequest<ByteBuf, ByteBuf> request = client.createGet(echoURL(uuid));
            Timer.Context ctx = timer.time();
            try {
                StringBuffer sb = executeBlocking2(request);
                ctx.stop();
            } catch (Exception e) {
                ctx.stop();
                errors.inc();
                e.printStackTrace();
            }
        }
    }

    @Test(priority = 4)
    public void testBlockingGETagain(){
        Timer timer = getTimingTimer(this.getClass(), "testBlockingGETagain");
        Counter errors = getErrorCounter(this.getClass(), "testBlockingGETagain");
        for (int i = 0; i < EXECUTIONS; i++){
            String uuid = UUID.randomUUID().toString();
            HttpClientRequest<ByteBuf, ByteBuf> request = client.createGet(echoURL(uuid));
            Timer.Context ctx = timer.time();
            try {
                StringBuffer sb = executeBlocking2(request);
                ctx.stop();
            } catch (Exception e) {
                ctx.stop();
                errors.inc();
                e.printStackTrace();
            }
        }
    }

    @Test(priority = 5)
    public void testNonBlockingGET(){
        System.out.println("testNonBlockingGET start");
        CountDownLatch latcher = new CountDownLatch(EXECUTIONS);
        Timer timer = getTimingTimer(this.getClass(), "testNonBlockingGET");
        Counter errors = getErrorCounter(this.getClass(), "testNonBlockingGET");
        for (int i = 0; i < EXECUTIONS; i++) {
            String uuid = UUID.randomUUID().toString();
            HttpClientRequest<ByteBuf, ByteBuf> request = client.createGet(echoURL(uuid));
            executeAsync(request, timer, errors, latcher, i).subscribe(message -> {});
        }
        //make sure that we don't leave this benchmark without waiting for it to finish
        try {
            latcher.await();
        } catch (InterruptedException e) {
            System.out.println("hey, don't interrupt me!");
            e.printStackTrace();
        }
        System.out.println("testNonBlockingGET end");
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 100, priority = 6)
    public void testMultiThreadedBlockingGET() {
        Timer timer = getTimingTimer(this.getClass(), "testMultiThreadedBlockingGET");
        Counter errors = getErrorCounter(this.getClass(), "testMultiThreadedBlockingGET");
        String uuid = UUID.randomUUID().toString();
        HttpClientRequest<ByteBuf, ByteBuf> request = client.createGet(echoURL(uuid));
        Timer.Context ctx = timer.time();
        try {
            StringBuffer sb = executeBlocking2(request);
            ctx.stop();
        } catch (Exception e) {
            ctx.stop();
            errors.inc();
            e.printStackTrace();
        }
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 100, priority = 7)
    public void testMultiThreadedBlockingShortShortPOST() {
        Timer timer = getTimingTimer(this.getClass(), "testMultiThreadedBlockingShortShortPOST");
        Counter errors = getErrorCounter(this.getClass(), "testMultiThreadedBlockingShortShortPOST");
        HttpClientRequest<ByteBuf, ByteBuf> request = client.createPost(ECHO_DELAY_POST_SHORT_URL);
        Timer.Context ctx = timer.time();
        try {
            StringBuffer sb = executeBlockingObservable(request.writeStringContent(Observable.just(Payloads.SHORT_JSON)));
            ctx.stop();
        } catch (Exception e) {
            ctx.stop();
            errors.inc();
            e.printStackTrace();
        }
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 100, priority = 8)
    public void testMultiThreadedBlockingLongLongPOST() {
        Timer timer = getTimingTimer(this.getClass(), "testMultiThreadedBlockingLongLongPOST");
        Counter errors = getErrorCounter(this.getClass(), "testMultiThreadedBlockingLongLongPOST");
        HttpClientRequest<ByteBuf, ByteBuf> request = client.createPost(ECHO_DELAY_POST_LONG_URL);
        Timer.Context ctx = timer.time();
        try {
            StringBuffer sb = executeBlockingObservable(request.writeStringContent(Observable.just(Payloads.LONG_JSON)));
            ctx.stop();
        } catch (Exception e) {
            ctx.stop();
            errors.inc();
            e.printStackTrace();
        }
    }

    @Test(priority = 9)
    public void testNonBlockingShortShortPOST(){
        System.out.println("starting testNonBlockingShortShortPOST");
        CountDownLatch latcher = new CountDownLatch(1);
        Timer timer = getTimingTimer(this.getClass(), "testNonBlockingShortShortPOST");
        Counter errors = getErrorCounter(this.getClass(), "testNonBlockingShortShortPOST");
        for (int i = 0; i < EXECUTIONS; i++){
            Observable<HttpClientResponse<ByteBuf>> request = client.createPost(ECHO_DELAY_POST_SHORT_URL).writeStringContent(Observable.just(Payloads.SHORT_JSON));
            executeAsync(request, timer, errors, latcher, i).subscribe();
        }
        try {
            latcher.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end testNonBlockingShortShortPOST");
    }

    @Test(priority = 10)
    public void testNonBlockingShortLongPOST(){
        System.out.println("starting testNonBlockingShortLongPOST");
        CountDownLatch latcher = new CountDownLatch(EXECUTIONS);
        Timer timer = getTimingTimer(this.getClass(), "testNonBlockingShortLongPOST");
        Counter errors = getErrorCounter(this.getClass(), "testNonBlockingShortLongPOST");
        for (int i = 0; i < EXECUTIONS; i++){
            Observable<HttpClientResponse<ByteBuf>> request = client.createPost(ECHO_DELAY_POST_LONG_URL).writeStringContent(Observable.just(Payloads.SHORT_JSON));
            executeAsync(request, timer, errors, latcher, i).subscribe();
        }
        try {
            latcher.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end testNonBlockingShortLongPOST");
    }

    @Test(priority = 11)
    public void testNonBlockingLongShortPOST(){
        System.out.println("starting testNonBlockingLongShortPOST");
        CountDownLatch latcher = new CountDownLatch(EXECUTIONS);
        Timer timer = getTimingTimer(this.getClass(), "testNonBlockingLongShortPOST");
        Counter errors = getErrorCounter(this.getClass(), "testNonBlockingLongShortPOST");
        for (int i = 0; i < EXECUTIONS; i++){
            Observable<HttpClientResponse<ByteBuf>> request = client.createPost(ECHO_DELAY_POST_SHORT_URL).writeStringContent(Observable.just(Payloads.LONG_JSON));
            executeAsync(request, timer, errors, latcher, i).subscribe();
        }
        try {
            latcher.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end testNonBlockingLongShortPOST");
    }

    @Test(priority = 12)
    public void testNonBlockingLongLongPOST(){
        System.out.println("starting testNonBlockingLongLongPOST");
        CountDownLatch latcher = new CountDownLatch(EXECUTIONS);
        Timer timer = getTimingTimer(this.getClass(), "testNonBlockingLongLongPOST");
        Counter errors = getErrorCounter(this.getClass(), "testNonBlockingLongLongPOST");
        for (int i = 0; i < EXECUTIONS; i++){
            Observable<HttpClientResponse<ByteBuf>> request = client.createPost(ECHO_DELAY_POST_LONG_URL).writeStringContent(Observable.just(Payloads.LONG_JSON));
            executeAsync(request, timer, errors, latcher, i).subscribe();
        }
        try {
            latcher.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end testNonBlockingLongLongPOST");
    }

    //NOTE: run this one last - don't yet have a good way to make sure it does not bleed into the next benchmark case
    @Test(invocationCount = EXECUTIONS, threadPoolSize = 100, priority = 13)
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
        HttpClientRequest<ByteBuf, ByteBuf> request = client.createGet(echoURL(uuid));
        executeAsync(request, timer, errors, testMultiThreadedNonBlockingGETLatch, count.getAndIncrement()).subscribe(message -> {});
    }
}
