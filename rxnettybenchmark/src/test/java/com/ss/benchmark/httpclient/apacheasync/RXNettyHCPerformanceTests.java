package com.ss.benchmark.httpclient.apacheasync;

import com.ss.benchmark.httpclient.HttpClient;
import com.ss.benchmark.httpclient.HCPerformanceTests;
import org.testng.annotations.Test;

/**
 * @author sharath.srinivasa
 */
@Test(groups = "performance")
public class RXNettyHCPerformanceTests extends HCPerformanceTests {

    @Override
    protected HttpClient getClient() {
        return new RXNettyHC();
    }
}

