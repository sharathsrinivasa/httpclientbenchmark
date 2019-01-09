package com.ss.benchmark.httpclient.reactornetty;

import com.ss.benchmark.httpclient.HttpClientEngine;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.timeout.ReadTimeoutHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufFlux;
import reactor.netty.resources.ConnectionProvider;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ReactorNettyEngine implements HttpClientEngine {
    reactor.netty.http.client.HttpClient client;

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
    public String blockingGET(String uri) {
        reactor.netty.http.client.HttpClient.RequestSender requestSender = client
                .request(HttpMethod.GET)
                .uri(uri);

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
    public String blockingPOST(String uri, String body) {

        return  client
                .headers(entries -> entries.add("Content-Type", "application/json" ))
                .post()
                .uri(uri)
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
    public CompletableFuture<String> nonblockingGET(String uri) {
        final CompletableFuture<String> cfResponse = new CompletableFuture<>();

        reactor.netty.http.client.HttpClient.RequestSender requestSender = client
                .request(HttpMethod.GET)
                .uri(uri);

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
    public CompletableFuture<String> nonblockingPOST(String uri, String body) {
        final CompletableFuture<String> cfResponse = new CompletableFuture<>();

        reactor.netty.http.client.HttpClient.ResponseReceiver<?> requestSender = client
                .post()
                .uri(uri)
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
