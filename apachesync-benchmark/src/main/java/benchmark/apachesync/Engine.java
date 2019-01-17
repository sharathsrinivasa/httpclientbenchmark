package benchmark.apachesync;

import com.ss.benchmark.httpclient.common.Exceptions;
import com.ss.benchmark.httpclient.common.HttpClientEngine;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class Engine implements HttpClientEngine {

    private CloseableHttpClient client;
    private RequestConfig requestConfig;
    private String baseUrl = null;

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    public void createClient(String host, int port) {

        this.baseUrl = url(host, port);

        requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setConnectionRequestTimeout(READ_TIMEOUT)
                .setSocketTimeout(READ_TIMEOUT)
                .build();

        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setMaxTotal(MAX_CONNECTION_POOL_SIZE);

        client = HttpClients.custom()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .build();
    }

    @Override
    public String blockingGET(String path) {
        final HttpGet request = new HttpGet(baseUrl + path);
        request.setConfig(requestConfig);
        return execute(request);
    }

    @Override
    public String blockingPOST(String path, String body) {
        final HttpPost request = new HttpPost(baseUrl + path);
        request.setConfig(requestConfig);
        StringEntity stringEntity = Exceptions.rethrowChecked(() -> new StringEntity(body));
        request.setEntity(stringEntity);
        return execute(request);
    }

    @Override
    public CompletableFuture<String> nonblockingGET(String path) {
        return async(() -> blockingGET(path));
    }

    @Override
    public CompletableFuture<String> nonblockingPOST(String path, String body) {
        return async(() -> blockingPOST(path, body));
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    private String execute(HttpUriRequest req) {
        return Exceptions.rethrowChecked(() -> {
            HttpResponse response = client.execute(req);
            return EntityUtils.toString(response.getEntity());
        });
    }

    private CompletableFuture<String> async(Supplier<String> supplier) {
        return CompletableFuture.supplyAsync(supplier, executorService);
    }
}
