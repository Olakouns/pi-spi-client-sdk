package io.github.razacki.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.github.PiSpiClient;
import io.github.PiSpiClientBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractIntegrationTest {
    protected static WireMockServer wireMockServer;
    protected static PiSpiClient client;

    public static final String API_KEY = "test-api-key-12345678901234567890";
    public static final String AUTH_TOKEN = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...";

    @BeforeAll
    static void startServer() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options()
                .dynamicPort()
                .usingFilesUnderDirectory("src/test/resources/integration/wiremock")
        );
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());

        client = PiSpiClientBuilder.builder()
                .serverUrl(wireMockServer.baseUrl())
                .clientId("test-client")
                .clientSecret("test-secret")
                .apiKey("test-api-key-12345678901234567890")
                .build();
    }

    @AfterAll
    static void stopServer() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }
}
