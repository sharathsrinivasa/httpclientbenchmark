package com.ss.benchmark.httpclient.reactornetty;

import com.ss.benchmark.httpclient.HC;
import com.ss.benchmark.httpclient.HCPerformanceTests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

/**
 * @author sharath.srinivasa
 */
@Test(groups = "performance")
public class ReactorNettyHCPerformanceTests extends HCPerformanceTests {

    private static final Logger logger = LoggerFactory.getLogger(ReactorNettyHCPerformanceTests.class);

    @Override
    protected HC getClient() {
        return new ReactorNettyHC();
    }
}

