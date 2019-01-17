package benchmark.asynchttpclient;

import com.ss.benchmark.httpclient.common.HttpClientEngine;
import org.asynchttpclient.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import static com.ss.benchmark.httpclient.common.Exceptions.rethrowChecked;

public class Engine implements HttpClientEngine {

    private AsyncHttpClient client;
    private String baseUrl;

    @Override
    public void createClient(String host, int port) {
        AsyncHttpClientConfig cf = new DefaultAsyncHttpClientConfig.Builder()
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setReadTimeout(READ_TIMEOUT)
                //.setRequestTimeout(REQUEST_TIMEOUT)
                .setMaxConnections(MAX_CONNECTION_POOL_SIZE)
                .setKeepAlive(true)
                //.setConnectionTtl(CONNECTION_TTL)
                .build();

        client = new DefaultAsyncHttpClient(cf);
        baseUrl = url(host, port);
    }

    @Override
    public String blockingGET(String path) {
        return rethrowChecked(() -> nonblockingGET(path).get());
    }

    @Override
    public String blockingPOST(String path, String body) {
        return rethrowChecked(() -> nonblockingPOST(path, body).get());
    }

    @Override
    public CompletableFuture<String> nonblockingGET(String path) {
        Request req = client.prepareGet(mkUrl(path)).build();
        return execute(req);
    }

    @Override
    public CompletableFuture<String> nonblockingPOST(String path, String body) {
        Request req = client.preparePost(mkUrl(path)).setBody(body).build();
        return execute(req);
    }

    private CompletableFuture<String> execute(Request request) {
        return client.executeRequest(request)
                .toCompletableFuture()
                .thenApply(resp -> {
                    if (resp.getStatusCode() != 200) {
                        // consume response and then throw exception
                        resp.getResponseBody();
                        throw new RuntimeException("Unexpected response code : " + resp.getStatusCode());
                    }
                    return rethrowChecked(() -> new String(resp.getResponseBodyAsBytes(), StandardCharsets.UTF_8));
                });
    }

    private String mkUrl(String path) {
        if (path.startsWith("/")) {
            return baseUrl + path;
        }
        return baseUrl + "/" + path;
    }


    @Override
    public void close() throws IOException {
        client.close();
    }
}
