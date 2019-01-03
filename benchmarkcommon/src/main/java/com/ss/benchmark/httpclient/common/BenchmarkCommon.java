package com.ss.benchmark.httpclient.common;

/**
 * Created by ssrinivasa on 12/13/18.
 */
public class BenchmarkCommon extends MetricsHelper {

    public static final String ECHO_DELAY_BASE_URL = "/echodelayserv/echo";
    public static final String ECHO_DELAY_SHORT_URL = "/echodelayserv/echo/short";
    public static final String ECHO_DELAY_LONG_URL = "/echodelayserv/echo/long";
    public static final String TEST_ENDPOINT = "/echodelayserv/echo/testmonkey";
    public static final String ECHO_DELAY_SETUP_FULL_URL = "/echodelayserv/delay/uniform?min=1ms&max=2ms";

    //All times are milliseconds unless otherwise noted
    public static final int MAX_CONNECTION_POOL_SIZE = 200;
    public static final int CONNECTION_TTL = 60000;
    public static final int CONNECT_TIMEOUT = 500;
    // set it as low as possible to make them same as rxnetty. AFAIK, there is no concept of connection acquire time in rxnetty.
    public static final int CONNECTION_REQUEST_TIMEOUT = 1;
    public static final int READ_TIMEOUT = 2000;

    public static final int MIN_CONNECTION_POOL_SIZE = 100;
    public static final int MAX_CONNECTION_PER_ROUTE = 200;
    public static final int REQUEST_TIMEOUT = 2000;
    public static final int SOCKET_TIMEOUT = 2000;

    protected static final int EXECUTIONS = 10000;

    protected void setupMetrics() {this.initializeMetrics();}
    protected void tearDownMetrics() {dumpMetrics(); closeMetrics();}
    protected String echoURL(String echophrase){return BenchmarkCommon.ECHO_DELAY_BASE_URL + "/" + echophrase;}
    protected String getBaseUrl() { return "http://localhost:8080";}

}
