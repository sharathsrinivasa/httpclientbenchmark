package com.ss.test.httpclient.reactornetty;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * @author sharath.srinivasa
 */
@Fork(1)
@Measurement(iterations = 2)
@Warmup(iterations = 2)
@BenchmarkMode(Mode.SampleTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Threads(16)
public class ReactorNettyHCPerformanceTests {
    private static final String ECHO_DELAY_BASE_URL = "/echodelayserv/echo/";
    private static final String BASE_URL = "http://localhost:8080";

    @State(Scope.Benchmark)
    public static class ReactorNettyState {
        private HttpClient client;
        private WireMockServer wireMockServer;

        @Setup(Level.Trial)
        public void setup() {
            client = HttpClient.create(ConnectionProvider.fixed("benchmark", 200))
                    .baseUrl(BASE_URL)
                    .tcpConfiguration(tcpClient -> tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 500)
                            .doOnConnected(con -> con.addHandlerLast(new ReadTimeoutHandler(500, TimeUnit.MILLISECONDS))));

            int cpus = Runtime.getRuntime().availableProcessors();

            WireMockConfiguration config = WireMockConfiguration.wireMockConfig()
                    .port(8080)
                    .httpsPort(9443)
                    .disableRequestJournal()
                    .containerThreads(200)
                    .jettyAcceptQueueSize(10)
                    .jettyAcceptors(cpus);

            wireMockServer = new WireMockServer(config);
            wireMockServer.stubFor(
                    get(urlMatching("/echodelayserv/echo/.*")).willReturn(aResponse().withStatus(200)
                            .withHeader("Content-Type", "plain/text")
                            .withBody("doesnotmatter")));
            wireMockServer.start();
        }

        @TearDown(Level.Trial)
        public void tearDown() {
            wireMockServer.stop();
        }
    }

    @Benchmark
    public void blockingGET(ReactorNettyState state) {
        executeSync(state.client
                .request(HttpMethod.GET)
                .uri(echoURL()));
    }

    @Benchmark
    public void multiThreadedBlockingGET(ReactorNettyState state) {
        executeSync(state.client
                .request(HttpMethod.GET)
                .uri(echoURL()));
    }

    @Benchmark
    public void nonBlockingGET(ReactorNettyState state) {
        executeAsync(state.client
                .request(HttpMethod.GET)
                .uri(echoURL()));
    }

    private void executeSync(HttpClient.ResponseReceiver<?> request) {
        request
                .responseSingle((res, body) -> {
                    if (res.status().code() != 200) {
                        Mono.error(new IllegalStateException("Unexpected response code : " + res.status().code()));
                    }
                    return body;
                })
                .map(byteBuf -> byteBuf.toString(StandardCharsets.UTF_8))
                .block();
    }

    private void executeAsync(final HttpClient.ResponseReceiver<?> request) {
        CountDownLatch latch = new CountDownLatch(1);
        request
                .responseSingle((res, body) -> {
                    if (res.status().code() != 200) {
                        Mono.error(new IllegalStateException("Unexpected response code : " + res.status().code()));
                    }
                    return body;
                })
                .map(byteBuf -> byteBuf.toString(StandardCharsets.UTF_8))
                .doOnTerminate(latch::countDown)
                .doOnError(Throwable::printStackTrace)
                .block();
        try {
            latch.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String echoURL() {
        return ECHO_DELAY_BASE_URL + "/" + UUID.randomUUID().toString();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ReactorNettyHCPerformanceTests.class.getSimpleName())
                .addProfiler(GCProfiler.class)
                .build();
        new Runner(opt).run();
    }
}
