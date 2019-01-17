package benchmark.asynchttpclient;

import com.ss.benchmark.httpclient.common.BasePerformanceTest;
import com.ss.benchmark.httpclient.common.HttpClientEngine;
import org.testng.annotations.Test;

/**
 * @author sharath.srinivasa
 */
@Test(groups = "performance")
public class PerformanceTests extends BasePerformanceTest {

    @Override
    protected HttpClientEngine getClient() {
        return new Engine();
    }
}

