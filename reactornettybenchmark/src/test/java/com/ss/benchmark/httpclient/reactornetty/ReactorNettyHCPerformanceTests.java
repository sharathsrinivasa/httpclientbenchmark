package com.ss.benchmark.httpclient.reactornetty;

import com.ss.benchmark.httpclient.HC;
import com.ss.benchmark.httpclient.HCPerformanceTests;
import org.testng.annotations.Test;

/**
 * @author sharath.srinivasa
 */
@Test(groups = "performance")
public class ReactorNettyHCPerformanceTests extends HCPerformanceTests {

    @Override
    protected HC getClient() {
        return new ReactorNettyHC();
    }
}

