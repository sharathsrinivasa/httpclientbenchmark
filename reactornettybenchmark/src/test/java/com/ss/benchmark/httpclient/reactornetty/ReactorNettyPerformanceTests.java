package com.ss.benchmark.httpclient.reactornetty;

import com.ss.benchmark.httpclient.HttpClientEngine;
import com.ss.benchmark.httpclient.PerformanceTests;
import org.testng.annotations.Test;

/**
 * @author sharath.srinivasa
 */
@Test(groups = "performance")
public class ReactorNettyPerformanceTests extends PerformanceTests {

    @Override
    protected HttpClientEngine getClient() {
        return new ReactorNettyEngine();
    }
}

