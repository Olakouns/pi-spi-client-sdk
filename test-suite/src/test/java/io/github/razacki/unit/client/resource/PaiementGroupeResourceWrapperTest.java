package io.github.razacki.unit.client.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.exception.PiSpiException;
import io.github.provider.JacksonProvider;
import io.github.razacki.TestUtils;
import io.github.representation.PaiementGroupeRepresentation;
import io.github.representation.PaiementGroupeRequest;
import io.github.representation.enums.DemandePaiementGroupeStatut;
import io.github.resource.PaiementGroupeResource;
import io.github.resource.wrapper.PaiementGroupeResourceWrapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.jupiter.api.*;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Tag("unit")
@DisplayName("PaiementGroupeResourceWrapper Tests")
public class PaiementGroupeResourceWrapperTest {
    private MockWebServer mockServer;
    private PaiementGroupeResourceWrapper wrapper;
    private Client client;
    private static ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockServer = new MockWebServer();
        mockServer.start();

        client = ClientBuilder.newClient().register(JacksonProvider.class);
        WebTarget rootTarget = client.target(mockServer.url("/paiements-groupes").toString());

        PaiementGroupeResource proxy = ((ResteasyWebTarget) rootTarget).proxy(PaiementGroupeResource.class);

        wrapper = new PaiementGroupeResourceWrapper(proxy, rootTarget);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (client != null) client.close();
        if (mockServer != null) mockServer.shutdown();
    }

    @Test
    @DisplayName("Should create a group payment (HTTP 202)")
    void shouldCreateGroupPayment() throws Exception {
        // Arrange
        mockServer.enqueue(new MockResponse().setResponseCode(202));
        String json = TestUtils.loadJson("/unit/mock-responses/create-paiement-groupe-response.json");
        PaiementGroupeRequest requestBody = objectMapper.readValue(json, PaiementGroupeRequest.class);

        // Act
        wrapper.create(requestBody);

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(request.getHeader(HttpHeaders.CONTENT_TYPE)).contains(MediaType.APPLICATION_JSON);
    }

    @Test
    @DisplayName("Should find group payment by instructionId")
    void shouldFindById() throws Exception {
        // Arrange
        String instructionId = "SALAIRES-2025-01";
        mockGetResponse(instructionId);

        // Act
        PaiementGroupeRepresentation result = wrapper.findById(instructionId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getInstructionId()).isEqualTo(instructionId);
        assertThat(result.getStatut()).isEqualTo(DemandePaiementGroupeStatut.INITIE);
        assertThat(result.getTransactionsTotal()).isEqualTo(500);

        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getPath()).endsWith("/" + instructionId);
        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
    }

    @Test
    @DisplayName("Should confirm group payment and return updated status")
    void shouldConfirmGroup() throws Exception {
        // Arrange
        String instructionId = "RTP-MASSE-2025-01";
        mockConfirmResponse(instructionId);

        // Act
        PaiementGroupeRepresentation result = wrapper.confirm(instructionId, true);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatut()).isEqualTo(DemandePaiementGroupeStatut.CONFIRME);
        assertThat(result.getTransactionsEnvoyees()).isEqualTo(7);

        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo(HttpMethod.PUT);
        assertThat(request.getBody().readUtf8()).contains("\"decision\":true");
    }

    @Test
    @DisplayName("Should throw exception when instructionId is empty on find")
    void shouldValidateEmptyId() {
        assertThatThrownBy(() -> wrapper.findById("  "))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("Instruction ID cannot be null or empty");
    }

    @Test
    @DisplayName("Should throw exception when request is null on create")
    void shouldValidateNullRequest() {
        assertThatThrownBy(() -> wrapper.create(null))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("PaiementGroupeRequest cannot be null");
    }


    private void mockGetResponse(String id) {
        String json = "{\n" +
                "  \"instructionId\": \"" + id + "\",\n" +
                "  \"statut\": \"INITIE\",\n" +
                "  \"dateDemande\": \"2024-10-17T10:00:00Z\",\n" +
                "  \"dateExpiration\": \"2024-10-18T10:00:00Z\",\n" +
                "  \"transactionsTotal\": 500,\n" +
                "  \"transactionsInitiees\": 498,\n" +
                "  \"transactionsEnvoyees\": 0,\n" +
                "  \"transactionsIrrevocables\": 0,\n" +
                "  \"transactionsRejetees\": 2\n" +
                "}";
        enqueueJson(json);
    }

    private void mockConfirmResponse(String id) {
        String json = "{\n" +
                "  \"instructionId\": \"" + id + "\",\n" +
                "  \"statut\": \"CONFIRME\",\n" +
                "  \"dateDemande\": \"2024-10-17T10:00:00Z\",\n" +
                "  \"dateConfirmation\": \"2024-10-17T10:05:00Z\",\n" +
                "  \"transactionsTotal\": 7,\n" +
                "  \"transactionsInitiees\": 0,\n" +
                "  \"transactionsEnvoyees\": 7,\n" +
                "  \"transactionsIrrevocables\": 0,\n" +
                "  \"transactionsRejetees\": 0\n" +
                "}";
        enqueueJson(json);
    }

    private void enqueueJson(String json) {
        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(200));
    }
}
