package com.ss.benchmark.httpclient;

import java.util.concurrent.CompletableFuture;

public interface HttpClientEngine {

    //All times are milliseconds unless otherwise noted
    int MAX_CONNECTION_POOL_SIZE = 200;
    int CONNECT_TIMEOUT = 5_000;
    int READ_TIMEOUT = 50_000;

    /**
     * HTTP protocol is assumed.
     */
    void createClient(String host, int port);

    String blockingGET(String uri);

    String blockingPOST(String uri, String body);

    CompletableFuture<String> nonblockingGET(String uri);

    CompletableFuture<String> nonblockingPOST(String uri, String body);

    default String url(String host, int port) {
        return "http://" + host + ":" + port;
    }
}
