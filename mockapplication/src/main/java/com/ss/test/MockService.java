package com.ss.test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * @author sharath.srinivasa
 */
public class MockService {

    private static final Logger logger = LoggerFactory.getLogger(MockService.class);

    private WireMockServer wireMockServer;

    private static String HTTP_PORT_DEFAULT = "8080";
    private static String HTTPS_PORT_DEFUALT = "9443";

    public MockService(){
        this("8080", "9443");
    }

    public MockService(String http_port, String https_port){

        Validate.isTrue(NumberUtils.isDigits(http_port), "Invalid value for http.port");
        Validate.isTrue(NumberUtils.isDigits(https_port), "Invalid value for https.port");

        int httpsPort = Integer.parseInt(https_port);
        int httpPort = Integer.parseInt(http_port);

        int cpus = Runtime.getRuntime().availableProcessors();
        logger.info("Available processors {}", cpus);

        WireMockConfiguration config = wireMockConfig()
                .port(httpPort)
                .httpsPort(httpsPort)
                .disableRequestJournal()
                .containerThreads(200)
                .jettyAcceptQueueSize(10)
                .jettyAcceptors(cpus);

        wireMockServer = new WireMockServer(config);
        wireMockServer.start();

        //configure the basic stubs
        createEchoStubs();
        logger.info("Completion of stubs generation");
    }

    public void stop(){
        wireMockServer.stop();
    }

    private void createEchoStubs(){
        wireMockServer.stubFor(
                      get(urlMatching("/echodelayserv/echo/.*"))
                      .willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(" {\n" +
                              "        \"path\": \"chivas\",\n" +
                              "                \"planned-delay\": 464,\n" +
                              "                \"real-delay\": 458\n" +
                              "        }")));
    }

    private static String getProperty(String name, String defaultValue){
        String ret = System.getProperty(name);
        if (ret == null){
            ret = System.getenv(name);
            if (ret == null){
                ret = defaultValue;
            }
        }
        return ret;
    }

    public static void main(String[] args){
        logger.info("Starting mock service...");

        String http_port = getProperty("http.port" , HTTP_PORT_DEFAULT);
        String https_port = getProperty("https.port", HTTPS_PORT_DEFUALT);
        new MockService(http_port, https_port);

        logger.info("Successfully started the mock service.");
    }


}
