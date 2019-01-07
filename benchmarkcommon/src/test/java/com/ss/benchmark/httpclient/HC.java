package com.ss.benchmark.httpclient;

import java.util.concurrent.CompletableFuture;

public interface HC {

    //All times are milliseconds unless otherwise noted
    static final int MAX_CONNECTION_POOL_SIZE = 200;
    static final int CONNECT_TIMEOUT = 500;
    static final int CONNECTION_REQUEST_TIMEOUT = 1;
    static final int READ_TIMEOUT = 2000;

    void setup(String baseUrl);

    String blockingGET(String uri);

    String blockingPOST(String uri, String body);

    CompletableFuture<String> nonblockingGET(String uri);

    CompletableFuture<String> nonblockingPOST(String uri, String body);

}
