package benchmark;

import com.ss.benchmark.httpclient.HCPerformanceTests;
import com.ss.benchmark.httpclient.HttpClient;
import org.testng.annotations.Test;

/**
 * @author sharath.srinivasa
 */
@Test(groups = "performance")
public class AsyncHttpClientPerformanceTests extends HCPerformanceTests {

    @Override
    protected HttpClient getClient() {
        return new AsyncHttpClientBenchmarkClient();
    }
}

