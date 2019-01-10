package benchmark;

import com.ss.benchmark.httpclient.common.HttpClientEngine;
import com.ss.benchmark.httpclient.common.BasePerformanceTest;
import org.testng.annotations.Test;

/**
 * @author sharath.srinivasa
 */
@Test(groups = "performance")
public class AsyncHttpClientBasePerformanceTest extends BasePerformanceTest {

    @Override
    protected HttpClientEngine getClient() {
        return new AsyncHttpClientEngine();
    }
}
