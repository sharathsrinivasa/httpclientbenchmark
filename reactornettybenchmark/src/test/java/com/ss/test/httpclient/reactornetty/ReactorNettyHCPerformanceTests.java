package com.ss.test.httpclient.reactornetty;

import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.testng.Assert.assertEquals;

/**
 * @author sharath.srinivasa
 */
@Test
public class ReactorNettyHCPerformanceTests {

    private static final String echoEndpointResponse = " {\n" +
            "        \"path\": \"chivas\",\n" +
            "                \"planned-delay\": 464,\n" +
            "                \"real-delay\": 458\n" +
            "        }";

    private static final Logger logger = LoggerFactory.getLogger(ReactorNettyHCPerformanceTests.class);

    private final String ECHO_DELAY_BASE_URL = "/echodelayserv/echo/";

    private static final int EXECUTIONS = 10000;

    HttpClient reactorNettyClient;

    AtomicInteger count = new AtomicInteger(0);
    private Set<CountDownLatch> latches = new HashSet<>();
    private Lock lock = new ReentrantLock();

    @BeforeTest
    public void initializeTest() {
        // build client instance
        reactorNettyClient = setupReactorNettyHttpClient(this.getBaseUrl());
    }

    // accepts a baseUrl as input and returns an instance of HttpClient
    private HttpClient setupReactorNettyHttpClient(String baseUrl) {
        return HttpClient
                .create(ConnectionProvider.fixed("benchmark", 200))
                .baseUrl(baseUrl)
                .tcpConfiguration(tcpClient ->
                        tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 500)
                                 .doOnConnected( con -> con.addHandlerLast(new ReadTimeoutHandler(500, TimeUnit.MILLISECONDS)))
                );
    }


    private String getBaseUrl() {
        return "http://localhost:8080";
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
    }

    @Test(priority = 1)
    public void blockingGET() {
        //using blocking requests here
        String uuid = UUID.randomUUID().toString();

        for (int i = 0; i < EXECUTIONS; i++) {
            HttpClient.RequestSender requestSender = reactorNettyClient
                    .request(HttpMethod.GET)
                    .uri(echoURL(uuid));

            try {
                String result = executeSync(requestSender);
                assertEquals(result, echoEndpointResponse);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 100)
    public void multiThreadedBlockingGET() {
        String uuid = UUID.randomUUID().toString();

            HttpClient.RequestSender requestSender = reactorNettyClient
                    .request(HttpMethod.GET)
                    .uri(echoURL(uuid));

            try {
                String result = executeSync(requestSender);
                assertEquals(result, echoEndpointResponse);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }


    @Test(priority = 2)
    public void testNonBlockingGET() {
        logger.info("testNonBlockingGET start");
        CountDownLatch latcher = new CountDownLatch(EXECUTIONS);
        for (int i = 0; i < EXECUTIONS; i++) {
            String uuid = UUID.randomUUID().toString();

            HttpClient.RequestSender requestSender = reactorNettyClient
                    .request(HttpMethod.GET)
                    .uri(echoURL(uuid));

           StepVerifier
                   .create(executeAsync(requestSender, latcher, i))
                   .assertNext(s -> assertEquals(s, echoEndpointResponse))
                   .verifyComplete();
        }
        try {
            latcher.await();
        } catch (InterruptedException e) {
            logger.error("hey, don't interrupt me!");
            e.printStackTrace();
        }
        logger.info("testNonBlockingGET end");
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
            final CountDownLatch latch,
            final int counter
    ) {
        return request
                .responseSingle((res, body) -> {
                    if (res.status().code() != 200) {
                        Mono.error(new IllegalStateException("Unexpected response code : " + res.status().code()));
                    }
                    return body;
                })
                .map(byteBuf -> byteBuf.toString(StandardCharsets.UTF_8))
                .log()
                .doOnTerminate(() -> {
                    if (latch != null) latch.countDown();
                })
                .doOnError(throwable -> {
                    logger.error("Failed : {}", counter);
                    throwable.printStackTrace();
                });
    }

    protected String echoURL(String echophrase){return ECHO_DELAY_BASE_URL + "/" + echophrase;}
}
