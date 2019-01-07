package com.ss.benchmark.httpclient.apacheasync;

import com.ss.benchmark.httpclient.HttpClient;

import java.util.concurrent.CompletableFuture;

public class RXNettyHC implements HttpClient {
    @Override
    public void createClient(String baseUrl) {

    }

    @Override
    public String blockingGET(String uri) {
        return null;
    }

    @Override
    public String blockingPOST(String uri, String body) {
        return null;
    }

    @Override
    public CompletableFuture<String> nonblockingGET(String uri) {
        return null;
    }

    @Override
    public CompletableFuture<String> nonblockingPOST(String uri, String body) {
        return null;
    }
}
