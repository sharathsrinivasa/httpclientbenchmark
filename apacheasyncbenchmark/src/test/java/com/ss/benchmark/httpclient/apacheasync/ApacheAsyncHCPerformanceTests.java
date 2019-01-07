package com.ss.benchmark.httpclient.apacheasync;

import com.ss.benchmark.httpclient.HttpClient;
import com.ss.benchmark.httpclient.HCPerformanceTests;
import org.testng.annotations.Test;

/**
 * @author sharath.srinivasa
 */
@Test(groups = "performance")
public class ApacheAsyncHCPerformanceTests extends HCPerformanceTests {

    @Override
    protected HttpClient getClient() {
        return new ApacheAsyncHC();
    }
}

