package com.ss.benchmark.httpclient.apacheasync;

import com.ss.benchmark.httpclient.HC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class ApacheAsyncHC implements HC {
    private static final Logger logger = LoggerFactory.getLogger(ApacheAsyncHC.class);

    @Override
    public void setup(String baseUrl) {

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
