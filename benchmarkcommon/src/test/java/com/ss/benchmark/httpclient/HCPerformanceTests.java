package com.ss.benchmark.httpclient;

import com.codahale.metrics.*;
import com.ss.benchmark.httpclient.common.Payloads;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
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
    protected static final String SERVER_HOST = "localhost";
    protected static final int SERVER_PORT = 8080;

    protected static final int EXECUTIONS = 1_000;
    protected static final int WORKERS = 100;

    private static final Logger LOGGER = LoggerFactory.getLogger(HCPerformanceTests.class);

    protected final MetricRegistry metricRegistry = new MetricRegistry();
    protected final ScheduledReporter reporter = ConsoleReporter.forRegistry(metricRegistry).convertDurationsTo(TimeUnit.MILLISECONDS).build();

    private Set<CountDownLatch> latches = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private HttpClient client;

    @BeforeTest
    public void beforeTest() {
        reporter.start(30, TimeUnit.SECONDS);

        client = getClient();

        client.createClient(SERVER_HOST, SERVER_PORT);

        // this is simply to warmup the connection pool
        LOGGER.debug("Start warmup");
        for (int i = 0; i < EXECUTIONS; i++) {
            syncGET(metricRegistry.timer(MetricRegistry.name(this.getClass(), "warmup", "timing")),
                    metricRegistry.counter(MetricRegistry.name(this.getClass(), "warmup", "errorRate")));
        }
        LOGGER.debug("Completed warmup");
    }

    @AfterTest
    public void afterTest() {
        reporter.report();
        reporter.close();
    }

    @BeforeMethod
    public void beforeMethod() {
    }

    @AfterMethod
    public void afterMethod() {
        for (CountDownLatch latcher : latches) {
            try {
                latcher.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 1, groups = {"sync", "blocking"})
    public void testSingleThreadedBlockingShortGET() {
        String method = myName();
        LOGGER.debug("Start " + method);

        syncGET(metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));

        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = WORKERS, groups = {"multithreaded", "blocking"})
    public void testMultiThreadedBlockingShortGET() {
        String method = myName();

        LOGGER.debug("Start " + method);

        syncGET(metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));

        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 1, groups = {"singlethreaded", "blocking"})
    public void testSingleThreadedBlockingShortShortPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        syncPOST(MOCK_SHORT_URL,
                Payloads.SHORT,
                Payloads.SHORT,
                metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));

        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = WORKERS, groups = {"multithreaded", "blocking"})
    public void testMultiThreadedBlockingShortShortPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        syncPOST(MOCK_SHORT_URL,
                Payloads.SHORT,
                Payloads.SHORT,
                metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));

        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 1, groups = {"singlethreaded", "blocking"})
    public void testSingleThreadedBlockingShortLongPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        syncPOST(MOCK_LONG_URL,
                Payloads.SHORT,
                Payloads.LONG,
                metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));

        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = WORKERS, groups = {"multithreaded", "blocking"})
    public void testMultiThreadedBlockingShortLongPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        syncPOST(MOCK_LONG_URL,
                Payloads.SHORT,
                Payloads.LONG,
                metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));

        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 1, groups = {"singlethreaded", "blocking"})
    public void testSingleThreadedBlockingLongLongPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        syncPOST(MOCK_LONG_URL,
                Payloads.LONG,
                Payloads.LONG,
                metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));

        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = WORKERS, groups = {"multithreaded", "blocking"})
    public void testMultiThreadedBlockingLongLongPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        syncPOST(MOCK_LONG_URL,
                Payloads.LONG,
                Payloads.LONG,
                metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));

        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 1, groups = {"singlethreaded", "nonblocking"})
    public void testSingleThreadedNonBlockingShortGET() {
        String method = myName();
        LOGGER.debug("Start " + method);


            asyncGET(new CountDownLatch(1),
                    metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                    metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));

        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = WORKERS, groups = {"multithreaded", "nonblocking"})
    public void testMultiThreadedNonBlockingShortGET() {
        String method = myName();
        LOGGER.debug("Start " + method);

        asyncGET(new CountDownLatch(1),
                metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));

        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 1, groups = {"singlethreaded", "nonblocking"})
    public void testSingleThreadedNonBlockingShortShortPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        asyncPOST(MOCK_SHORT_URL,
                Payloads.SHORT,
                Payloads.SHORT,
                new CountDownLatch(1),
                metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));


        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = WORKERS, groups = {"multithreaded", "nonblocking"})
    public void testMultiThreadedNonBlockingShortShortPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        asyncPOST(MOCK_SHORT_URL,
                Payloads.SHORT,
                Payloads.SHORT,
                new CountDownLatch(1),
                metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));

        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 1, groups = {"singlethreaded", "nonblocking"})
    public void testSingleThreadedNonBlockingShortLongPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        asyncPOST(MOCK_LONG_URL,
                Payloads.SHORT,
                Payloads.LONG,
                new CountDownLatch(1),
                metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));


        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = WORKERS, groups = {"multithreaded", "nonblocking"})
    public void testMultiThreadedNonBlockingShortLongPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        asyncPOST(MOCK_LONG_URL,
                Payloads.SHORT,
                Payloads.LONG,
                new CountDownLatch(1),
                metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));

        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = 1, groups = {"singlethreaded", "nonblocking"})
    public void testSingleThreadedNonBlockingLongLongPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        asyncPOST(MOCK_LONG_URL,
                Payloads.LONG,
                Payloads.LONG,
                new CountDownLatch(1),
                metricRegistry.timer(MetricRegistry.name(this.getClass(), method, "timing")),
                metricRegistry.counter(MetricRegistry.name(this.getClass(), method, "errorRate")));

        LOGGER.debug("Completed " + method);
    }

    @Test(invocationCount = EXECUTIONS, threadPoolSize = WORKERS, groups = {"multithreaded", "nonblocking"})
    public void testMultiThreadedNonBlockingLongLongPOST() {
        String method = myName();
        LOGGER.debug("Start " + method);

        asyncPOST(MOCK_LONG_URL,
                Payloads.LONG,
                Payloads.LONG,
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
            if (ex != null || !expect.equals(result)) {
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



