package com.ss.benchmark.httpclient.reactornetty;

import com.ss.benchmark.httpclient.HttpClient;
import com.ss.benchmark.httpclient.HCPerformanceTests;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

/**
 * @author sharath.srinivasa
 */
@Test(groups = "performance")
public class ReactorNettyHCPerformanceTests extends HCPerformanceTests {

    @Override
    protected HttpClient getClient() {
        return new ReactorNettyHC();
    }
}

