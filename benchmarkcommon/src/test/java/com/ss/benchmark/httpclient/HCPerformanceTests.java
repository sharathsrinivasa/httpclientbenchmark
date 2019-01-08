package com.ss.benchmark.httpclient;

import com.codahale.metrics.*;
import com.ss.benchmark.httpclient.common.Payloads;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author sharath.srinivasa
 */
@Test(groups = "performance")
public abstract class HCPerformanceTests {

    protected static final String HELLO_URL = "/hello";
    protected static final String MOCK_SHORT_URL = "/short";
    protected static final String MOCK_LONG_URL = "/long";
    protected static final String BASE_URL = "http://localhost:8080";

    protected static final int EXECUTIONS = 100;


    private static final Logger LOGGER = LoggerFactory.getLogger(HCPerformanceTests.class);

    protected final MetricRegistry metricRegistry = new MetricRegistry();
    protected final ScheduledReporter reporter = ConsoleReporter.forRegistry(metricRegistry).convertDurationsTo(TimeUnit.MILLISECONDS).build();


    private Set<CountDownLatch> latches = new HashSet<>();

    private HttpClient client;

    @BeforeTest
    public void initializeTest() {
        reporter.start(1, TimeUnit.HOURS);

        client = getClient();

        client.createClient(BASE_URL);

        // this is simply to warmup the connection pool
        LOGGER.debug("Start warmup");
        for (int i = 0; i < EXECUTIONS; i++) {
            syncGET(metricRegistry.timer(MetricRegistry.name(this.getClass(), "warmup", "timing")),
                    metricRegistry.counter(MetricRegistry.name(this.getClass(), "warmup", "errorRate")));
        }
        LOGGER.debug("Completed warmup");
    }

    @AfterTest
    public void finalizeTest() {
        for (CountDownLatch latcher : latches) {
            try {
                latcher.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        reporter.report();
        reporter.close();
    }

    @Test(groups = {"sync", "blocking"})
    public void testSyncBlockingShortGET() {
        String method = myName();
        LOGGER.debug("Start " + method);

        for (int i = 0; i < EXECUTIONS; i++) {
            syncGET(metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                    metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));
        }

        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 100, groups = {"async", "blocking"})
    public void testAsyncBlockingShortGET() {
        String method = myName();

        LOGGER.debug("Start " + method);

        syncGET(metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));

        LOGGER.debug("Completed " + method);
    }

    @Test(groups = {"sync", "blocking"})
    public void testSyncBlockingShortShortPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        for (int i = 0; i < EXECUTIONS; i++) {
            syncPOST(MOCK_SHORT_URL,
                    Payloads.SHORT_JSON,
                    Payloads.SHORT_JSON,
                    metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                    metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));
        }

        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 100, groups = {"async", "blocking"})
    public void testAsyncBlockingShortShortPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        syncPOST(MOCK_SHORT_URL,
                Payloads.SHORT_JSON,
                Payloads.SHORT_JSON,
                metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));

        LOGGER.debug("Completed " + method);
    }

    @Test(groups = {"sync", "blocking"})
    public void testSyncBlockingShortLongPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        for (int i = 0; i < EXECUTIONS; i++) {
            syncPOST(MOCK_LONG_URL,
                    Payloads.SHORT_JSON,
                    Payloads.LONG_JSON,
                    metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                    metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));
        }
        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 100, groups = {"async", "blocking"})
    public void testAsyncBlockingShortLongPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        syncPOST(MOCK_LONG_URL,
                Payloads.SHORT_JSON,
                Payloads.LONG_JSON,
                metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));

        LOGGER.debug("Completed " + method);
    }

    @Test(groups = {"sync", "blocking"})
    public void testSyncBlockingLongLongPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        for (int i = 0; i < EXECUTIONS; i++) {
            syncPOST(MOCK_LONG_URL,
                    Payloads.LONG_JSON,
                    Payloads.LONG_JSON,
                    metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                    metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));
        }
        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 100, groups = {"async", "blocking"})
    public void testAsyncBlockingLongLongPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        syncPOST(MOCK_LONG_URL,
                Payloads.LONG_JSON,
                Payloads.LONG_JSON,
                metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));

        LOGGER.debug("Completed " + method);
    }

    @Test(groups = {"sync", "nonblocking"})
    public void testSyncNonBlockingShortGET() {
        String method = myName();
        LOGGER.debug("Start " + method);

        CountDownLatch latch = new CountDownLatch(EXECUTIONS);

        for (int i = 0; i < EXECUTIONS; i++) {
            asyncGET(latch,
                    metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                    metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));
        }

        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 100, groups = {"async", "nonblocking"})
    public void testAsyncNonBlockingShortGET() {
        String method = myName();
        LOGGER.debug("Start " + method);

        asyncGET(new CountDownLatch(1),
                metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));

        LOGGER.debug("Completed " + method);
    }

    @Test(groups = {"sync", "nonblocking"})
    public void testSyncNonBlockingShortShortPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        CountDownLatch latch = new CountDownLatch(EXECUTIONS);

        for (int i = 0; i < EXECUTIONS; i++) {
            asyncPOST(MOCK_SHORT_URL,
                    Payloads.SHORT_JSON,
                    Payloads.SHORT_JSON,
                    latch,
                    metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                    metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));
        }

        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 100, groups = {"async", "nonblocking"})
    public void testAsyncNonBlockingShortShortPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        asyncPOST(MOCK_SHORT_URL,
                Payloads.SHORT_JSON,
                Payloads.SHORT_JSON,
                new CountDownLatch(1),
                metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));

        LOGGER.debug("Completed " + method);
    }

    @Test(groups = {"sync", "nonblocking"})
    public void testSyncNonBlockingShortLongPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        CountDownLatch latch = new CountDownLatch(EXECUTIONS);

        for (int i = 0; i < EXECUTIONS; i++) {
            asyncPOST(MOCK_LONG_URL,
                    Payloads.SHORT_JSON,
                    Payloads.LONG_JSON,
                    latch,
                    metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                    metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));
        }

        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 100, groups = {"async", "nonblocking"})
    public void testAsyncNonBlockingShortLongPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        asyncPOST(MOCK_LONG_URL,
                Payloads.SHORT_JSON,
                Payloads.LONG_JSON,
                new CountDownLatch(1),
                metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));

        LOGGER.debug("Completed " + method);
    }

    @Test(groups = {"sync", "nonblocking"})
    public void testSyncNonBlockingLongLongPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        CountDownLatch latch = new CountDownLatch(EXECUTIONS);

        for (int i = 0; i < EXECUTIONS; i++) {
            asyncPOST(MOCK_LONG_URL,
                    Payloads.LONG_JSON,
                    Payloads.LONG_JSON,
                    latch,
                    metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                    metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));
        }

        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 100, groups = {"async", "nonblocking"})
    public void testAsyncNonBlockingLongLongPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        asyncPOST(MOCK_LONG_URL,
                Payloads.LONG_JSON,
                Payloads.LONG_JSON,
                new CountDownLatch(1),
                metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));

        LOGGER.debug("Completed " + method);
    }


    protected static String myName() {
        return StackWalker
                .getInstance(java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(s -> s.skip(1).limit(1).collect(Collectors.toList()))
                .get(0).getMethodName();
    }

    protected void asyncGET(CountDownLatch latch, Timer timer, Counter errors) {
        latches.add(latch);

        Timer.Context ctx = timer.time();

        CompletableFuture<String> cf = client.nonblockingGET(HELLO_URL);
        cf.handle((result, ex) -> {
            ctx.stop();
            if (!Payloads.HELLO.equals(result)) {
                errors.inc();
            }
            latch.countDown();
            return result;
        });
    }

    protected void asyncPOST(String url, String payload, String expect, CountDownLatch latch, Timer timer, Counter errors) {
        if (expect == null)
            throw new IllegalArgumentException("expected response payload can not be null");

        latches.add(latch);

        Timer.Context ctx = timer.time();

        CompletableFuture<String> cf = client.nonblockingPOST(url, payload);
        cf.handle((result, ex) -> {
            ctx.stop();
            if (!expect.equals(result)) {
                errors.inc();
            }
            latch.countDown();
            return result;
        });
    }

    protected void syncGET(Timer timer, Counter errors) {
        Timer.Context ctx = timer.time();
        String response = null;
        try {
            response = client.blockingGET(HELLO_URL);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            ctx.stop();
            if (!Payloads.HELLO.equals(response))
                errors.inc();
        }
    }

    protected void syncPOST(String url, String payload, String expect, Timer timer, Counter errors) {
        if (expect == null)
            throw new IllegalArgumentException("expected response payload can not be null");

        Timer.Context ctx = timer.time();
        String response = null;
        try {
            response = client.blockingPOST(url, payload);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        } finally {
            ctx.stop();
            if (!expect.equals(response))
                errors.inc();
        }
    }

    protected abstract HttpClient getClient();
}



