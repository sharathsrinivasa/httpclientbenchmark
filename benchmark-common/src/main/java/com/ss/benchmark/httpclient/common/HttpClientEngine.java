package com.ss.benchmark.httpclient.common;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import static com.ss.benchmark.httpclient.common.Exceptions.rethrowChecked;

public interface HttpClientEngine extends Closeable {

    //All times are milliseconds unless otherwise noted
    int MAX_CONNECTION_POOL_SIZE = 200;
    int CONNECT_TIMEOUT = 5_000;
    int READ_TIMEOUT = 50_000;

    /**
     * HTTP protocol is assumed.
     */
    void createClient(String host, int port);

    CompletableFuture<String> nonblockingGET(String path);

    CompletableFuture<String> nonblockingPOST(String path, String body);

    default String blockingGET(String path) {
        return rethrowChecked(() -> nonblockingGET(path).get());
    }

    default String blockingPOST(String path, String body) {
        return rethrowChecked(() -> nonblockingPOST(path, body).get());
    }

    default String url(String host, int port) {
        return "http://" + host + ":" + port;
    }

    @Override
    default void close() throws IOException {}


}
