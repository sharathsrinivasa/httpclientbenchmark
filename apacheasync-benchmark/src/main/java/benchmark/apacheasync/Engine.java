package benchmark.apacheasync;

import com.ss.benchmark.httpclient.common.HttpClientEngine;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.util.EntityUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class Engine implements HttpClientEngine {
    private CloseableHttpAsyncClient client;

    private RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(CONNECT_TIMEOUT)
            .setSocketTimeout(READ_TIMEOUT)
            .build();

    String baseUrl = null;

    @Override
    public void createClient(String host, int port) {
        this.baseUrl = url(host, port);
        HttpAsyncClientBuilder httpAsyncClientBuilder = HttpAsyncClients.custom();
        httpAsyncClientBuilder
                .setMaxConnTotal(MAX_CONNECTION_POOL_SIZE);
        client = httpAsyncClientBuilder.build();
        client.start();
    }

    @Override
    public String blockingGET(String uri) {
        final HttpGet request = new HttpGet(baseUrl + uri);
        request.setConfig(requestConfig);
        Future<HttpResponse> responseFuture = client.execute(request, null);
        try {
            HttpResponse httpResponse = responseFuture.get();
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String blockingPOST(String uri, String body) {
        final StringEntity stringEntity = new StringEntity(body, "UTF-8");
        final HttpPost request = new HttpPost(baseUrl + uri);

        request.addHeader("content-type", "application/json");
        stringEntity.setContentType("application/json");
        request.setEntity(stringEntity);
        request.setConfig(requestConfig);
        Future<HttpResponse> responseFuture = client.execute(request, null);
        try {
            HttpResponse httpResponse = responseFuture.get();
            return EntityUtils.toString(httpResponse.getEntity());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public CompletableFuture<String> nonblockingGET(String uri) {
        final CompletableFuture<String> cfResponse = new CompletableFuture<>();

        HttpGet request = new HttpGet(baseUrl + uri);
        request.setConfig(requestConfig);

        client.execute(request, new FutureCallback<>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                try {
                    cfResponse.complete(EntityUtils.toString(httpResponse.getEntity()));
                } catch (Exception e) {
                    cfResponse.completeExceptionally(e);
                }
            }

            @Override
            public void failed(Exception e) {
                cfResponse.completeExceptionally(e);
            }

            @Override
            public void cancelled() {
                cfResponse.cancel(true);
            }
        });
        return cfResponse;
    }

    @Override
    public CompletableFuture<String> nonblockingPOST(String uri, String body) {
        final CompletableFuture<String> cfResponse = new CompletableFuture<>();

        HttpPost request = new HttpPost(baseUrl + uri);
        request.addHeader("content-type", "application/json");
        request.addHeader("Host", "localhost");
        StringEntity stringEntity = new StringEntity(body, "UTF-8");
        stringEntity.setContentType("application/json");
        request.setEntity(stringEntity);

        client.execute(request, new FutureCallback<>() {
            @Override
            public void completed(HttpResponse httpResponse) {
                try {
                    cfResponse.complete(EntityUtils.toString(httpResponse.getEntity()));
                } catch (Exception e) {
                    cfResponse.completeExceptionally(e);
                }
            }

            @Override
            public void failed(Exception e) {
                cfResponse.completeExceptionally(e);
            }

            @Override
            public void cancelled() {
                cfResponse.cancel(true);
            }
        });
        return cfResponse;

    }
}
