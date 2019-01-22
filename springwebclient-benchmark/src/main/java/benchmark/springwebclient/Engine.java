package benchmark.springwebclient;

import com.ss.benchmark.httpclient.common.HttpClientEngine;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Engine implements HttpClientEngine {

    private WebClient webClient;

    @Override
    public void createClient(String host, int port) {
        String baseUrl = url(host, port);
        webClient = WebClient
                .builder()
                .clientConnector(new ReactorClientHttpConnector(createReactorNettyClient()))
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public String blockingGET(String path) {
        return performGET(path)
                .doOnError(t -> System.err.println("Failed requesting server: " + t.getMessage()))
                .block();
         }

    @Override
    public String blockingPOST(String path, String reqPayload) {
        return performPOST(path, reqPayload )
                .doOnError(t -> System.err.println("Failed requesting server: " + t.getMessage()))
                .block();
    }

    @Override
    public CompletableFuture<String> nonblockingGET(String path) {
        return  performGET(path)
                .toFuture();
    }

    @Override
    public CompletableFuture<String> nonblockingPOST(String path, String reqPayload) {
        return performPOST(path, reqPayload)
                .toFuture();
    }

    private Mono<String> consumeResponse(ClientResponse res) {
        if(res.rawStatusCode() != 200) {
            Mono.error(new RuntimeException("Unexpected response code : " + res.rawStatusCode()));
        }
        return res.bodyToMono(String.class);
    }

    private Mono<String> performGET(String path) {
        return webClient
                .get()
                .uri(path)
                .exchange()
                .flatMap(resp -> consumeResponse(resp));
    }

    private Mono<String> performPOST(String path, String reqPayload) {
        return webClient
                .method(HttpMethod.POST)
                .uri(path)
                .body(BodyInserters.fromPublisher(Mono.just(reqPayload), String.class ))
                .exchange()
                .flatMap(resp -> consumeResponse(resp));
    }

    private HttpClient createReactorNettyClient() {
        return HttpClient
                .create(ConnectionProvider.fixed("benchmark", MAX_CONNECTION_POOL_SIZE))
                .tcpConfiguration(tcpClient ->
                    tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,CONNECT_TIMEOUT )
                            .doOnConnected(con -> con.addHandlerLast(new ReadTimeoutHandler(READ_TIMEOUT, TimeUnit.MILLISECONDS)))
                );
    }
}
