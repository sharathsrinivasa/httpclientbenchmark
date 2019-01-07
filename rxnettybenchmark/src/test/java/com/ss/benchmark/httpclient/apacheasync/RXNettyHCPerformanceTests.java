package com.ss.benchmark.httpclient.apacheasync;

import com.ss.benchmark.httpclient.HC;
import com.ss.benchmark.httpclient.HCPerformanceTests;
import org.testng.annotations.Test;

/**
 * @author sharath.srinivasa
 */
@Test(groups = "performance")
public class RXNettyHCPerformanceTests extends HCPerformanceTests {

    @Override
    protected HC getClient() {
        return new RXNettyHC();
    }
}

