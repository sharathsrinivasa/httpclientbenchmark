package benchmark.rxnetty;

import com.ss.benchmark.httpclient.common.HttpClientEngine;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelOption;
import io.reactivex.netty.client.Host;
import io.reactivex.netty.client.pool.PoolConfig;
import io.reactivex.netty.client.pool.SingleHostPoolingProviderFactory;
import io.reactivex.netty.protocol.http.client.HttpClient;
import io.reactivex.netty.protocol.http.client.HttpClientResponse;
import rx.Observable;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Engine implements HttpClientEngine {

    private HttpClient<ByteBuf, ByteBuf> client;

    @Override
    public void createClient(String host, int port) {
        PoolConfig<ByteBuf, ByteBuf> poolConfig = new PoolConfig<ByteBuf, ByteBuf>().maxConnections(MAX_CONNECTION_POOL_SIZE);/* leave this out? .maxIdleTimeoutMillis(CONNECTION_TTL);*/
        client = HttpClient
                .newClient(SingleHostPoolingProviderFactory.create(poolConfig),
                        Observable.just(new Host(new InetSocketAddress(host, port)))).readTimeOut(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .channelOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT);
    }

    @Override
    public String blockingGET(String uri) {
        return businessLogic(client.createGet(uri)).toBlocking().first();
    }

    @Override
    public String blockingPOST(String uri, String body) {
        return businessLogic(mkPost(uri, body)).toBlocking().first();
    }

    @Override
    public CompletableFuture<String> nonblockingGET(String uri) {
        return toCompletableFuture(businessLogic(client.createGet(uri)));
    }

    @Override
    public CompletableFuture<String> nonblockingPOST(String uri, String body) {
        return toCompletableFuture(businessLogic(mkPost(uri, body)));
    }

    private Observable<HttpClientResponse<ByteBuf>> mkPost(String uri, String body) {
        return client.createPost(uri).writeStringContent(Observable.just(body));
    }

    private Observable<String> businessLogic(Observable<HttpClientResponse<ByteBuf>> request) {
        final StringBuffer content = new StringBuffer();
        return request.flatMap(response -> {
            int status = response.getStatus().code();
            if (status != 200) {
                throw new IllegalStateException("Unexpected response code: " + status);
            }
            return response.getContent();
        }).map(buffer -> {
            byte[] bytes = new byte[buffer.readableBytes()];
            buffer.readBytes(bytes);
            buffer.release();
            return new String(bytes);
        }).collect(() -> content, StringBuffer::append)
        .map(Object::toString);
    }

    private CompletableFuture<String> toCompletableFuture(Observable<String> businessLogic) {
         final CompletableFuture<String> future = new CompletableFuture<>();
        // Credit to https://www.nurkiewicz.com/2014/11/converting-between-completablefuture.html
        businessLogic
                .doOnError(future::completeExceptionally)
                .single()
                .forEach(future::complete);

        return future;
    }
}
