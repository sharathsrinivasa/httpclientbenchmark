package benchmark.reactornetty;

import com.ss.benchmark.httpclient.common.HttpClientEngine;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.timeout.ReadTimeoutHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Engine implements HttpClientEngine {
    HttpClient client;

    @Override
    public void createClient(String host, int port) {
        String baseURL = url(host, port);
        client = reactor.netty.http.client.HttpClient
                .create(ConnectionProvider.fixed("benchmark", MAX_CONNECTION_POOL_SIZE))
                .baseUrl(baseURL)
                .tcpConfiguration(tcpClient ->
                        tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT)
                                .doOnConnected(con -> con.addHandlerLast(new ReadTimeoutHandler(READ_TIMEOUT, TimeUnit.MILLISECONDS))));
    }

    @Override
    public String blockingGET(String path) {
        reactor.netty.http.client.HttpClient.RequestSender requestSender = client
                .request(HttpMethod.GET)
                .uri(path);

        return requestSender
                .responseSingle((res, body) -> {
                    if (res.status().code() != 200) {
                        Mono.error(new IllegalStateException("Unexpected response code : " + res.status().code()));
                    }
                    return body;
                })
                .map(byteBuf -> byteBuf.toString(StandardCharsets.UTF_8))
                .doOnError(t -> System.err.println("Failed requesting server: " + t.getMessage()))
                .block();
    }

    @Override
    public String blockingPOST(String path, String body) {

        return  client
                .headers(entries -> entries.add("Content-Type", "application/json" ))
                .post()
                .uri(path)
                .send(ByteBufFlux.fromString(Flux.just(body)))
                .responseSingle((res, responseBody) -> {
                    if (res.status().code() != 200) {
                        Mono.error(new IllegalStateException("Unexpected response code : " + res.status().code()));
                    }
                    return responseBody;
                })
                .map(byteBuf -> byteBuf.toString(StandardCharsets.UTF_8))
                .doOnError(t -> System.err.println("Failed requesting server: " + t.getMessage()))
                .block();
    }

    @Override
    public CompletableFuture<String> nonblockingGET(String path) {
        final CompletableFuture<String> cfResponse = new CompletableFuture<>();

        reactor.netty.http.client.HttpClient.RequestSender requestSender = client
                .request(HttpMethod.GET)
                .uri(path);

        requestSender
                .responseSingle((res, body) -> {
                    if (res.status().code() != 200) {
                        Mono.error(new IllegalStateException("Unexpected response code : " + res.status().code()));
                    }
                    return body;
                })
                .map(byteBuf -> byteBuf.toString(StandardCharsets.UTF_8))
                .doOnError(t -> cfResponse.completeExceptionally(t))
                .subscribe(res -> cfResponse.complete(res),
                        null,
                        null);

        return cfResponse;
    }

    @Override
    public CompletableFuture<String> nonblockingPOST(String path, String body) {
        final CompletableFuture<String> cfResponse = new CompletableFuture<>();

        reactor.netty.http.client.HttpClient.ResponseReceiver<?> requestSender = client
                .post()
                .uri(path)
                .send(ByteBufFlux.fromString(Flux.just(body)));

        requestSender
                .responseSingle((res, resBody) -> {
                    if (res.status().code() != 200) {
                        Mono.error(new IllegalStateException("Unexpected response code : " + res.status().code()));
                    }
                    return resBody;
                })
                .map(byteBuf -> byteBuf.toString(StandardCharsets.UTF_8))
                .doOnError(t -> cfResponse.completeExceptionally(t))
                .subscribe(res -> cfResponse.complete(res),
                        null,
                        null);

        return cfResponse;
    }

}
