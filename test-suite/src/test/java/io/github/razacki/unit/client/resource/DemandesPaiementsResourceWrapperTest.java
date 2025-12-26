package io.github.razacki.unit.client.resource;

import io.github.exception.PiSpiException;
import io.github.provider.JacksonProvider;
import io.github.razacki.TestUtils;
import io.github.representation.DemandesPaiementsRepresentation;
import io.github.representation.PagedResponse;
import io.github.representation.enums.DemandesPaiementsStatut;
import io.github.resource.DemandesPaiementsResource;
import io.github.resource.wrapper.DemandesPaiementsResourceWrapper;
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
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Tag("unit")
@DisplayName("DemandesPaiementsResourceWrapper Tests")
public class DemandesPaiementsResourceWrapperTest {
    private MockWebServer mockServer;
    private DemandesPaiementsResourceWrapper wrapper;
    private Client client;

    @BeforeEach
    void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();

        client = ClientBuilder.newClient().register(JacksonProvider.class);
        WebTarget rootTarget = client.target(mockServer.url("/demandes-paiements").toString());

        DemandesPaiementsResource proxy = ((ResteasyWebTarget) rootTarget).proxy(DemandesPaiementsResource.class);

        wrapper = new DemandesPaiementsResourceWrapper(proxy, rootTarget);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (client != null) client.close();
        if (mockServer != null) mockServer.shutdown();
    }

    @Test
    @DisplayName("Should list payment requests with pagination")
    void shouldListPaymentRequests() throws Exception {
        // Arrange
        mockPagedResponse();

        // Act
        PagedResponse<DemandesPaiementsRepresentation> response = wrapper.list(0, 4);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getData()).hasSize(4);
        assertThat(response.getData().get(0).getTxId()).isEqualTo("RTP-2023-001");

        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(request.getPath()).contains("page=0").contains("size=4");
    }

    @Test
    @DisplayName("Should create a payment request")
    void shouldCreatePaymentRequest() throws Exception {
        // Arrange
        DemandesPaiementsRepresentation body = new DemandesPaiementsRepresentation();
        body.setTxId("23511722");
        body.setMontant(BigDecimal.valueOf(3000000));

        mockCreateResponse();

        // Act
        DemandesPaiementsRepresentation result = wrapper.create(body);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTxId()).isEqualTo("23511722");
        assertThat(result.getStatut()).isEqualTo(DemandesPaiementsStatut.ENVOYE);

        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo(HttpMethod.POST);
    }

    @Test
    @DisplayName("Should find payment request by ID")
    void shouldFindById() throws Exception {
        // Arrange
        mockGetResponse();

        // Act
        DemandesPaiementsRepresentation result = wrapper.findById("23511722");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTxId()).isEqualTo("23511722");

        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getPath()).endsWith("/23511722");
    }

    @Test
    @DisplayName("Should confirm a payment request")
    void shouldConfirmRequest() throws Exception {
        // Arrange
        mockConfirmResponse();

        // Act
        DemandesPaiementsRepresentation result = wrapper.confirm("23552722", true);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getConfirmation()).isTrue();

        RecordedRequest request = mockServer.takeRequest();
        // Assuming the proxy maps confirm to /confirm or similar; here we check method & payload
        assertThat(request.getMethod()).isEqualTo(HttpMethod.PUT);
        assertThat(request.getBody().readUtf8()).contains("\"decision\":true");
    }

    @Test
    @DisplayName("Should send decision for a payment request")
    void shouldSendDecision() throws Exception {
        // Arrange
        mockSendDecisionResponse();

        // Act
        DemandesPaiementsRepresentation result = wrapper.sendDecision("23552722", true);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getStatut()).isEqualTo(DemandesPaiementsStatut.ENVOYE);

        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo(HttpMethod.PUT);
    }

    @Test
    @DisplayName("Should validate null input on create")
    void shouldValidateNullOnCreate() {
        assertThatThrownBy(() -> wrapper.create(null))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("demandesPaiementsRepresentation cannot be null");
    }

    @Test
    @DisplayName("Should query payment requests with complex filters")
    void shouldQueryWithFilters() throws Exception {
        // Arrange: Prepare a mocked paged response
        mockPagedResponse();

        // Act: Use the fluent API to build a filtered query
        PagedResponse<DemandesPaiementsRepresentation> response = wrapper.query()
                .page("0")
                .size(10)
                .filter(f -> f
                        .eq("statut", "IRREVOCABLE")
                        .eq("payeurAlias", "9b1b3499-3e50-435b-b757-ac7a83d8aa96")
                        .sortDesc("dateDemande")
                )
                .execute();

        // Assert: Basic response check
        assertThat(response).isNotNull();

        // Verify: Capture and decode the URL to verify query parameters
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        String path = URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name());
        // Verify standard pagination params
        assertThat(path).contains("page=0");
        assertThat(path).contains("size=10");

        // Verify custom filters (using the [eq] syntax of your ListQueryBuilder)
        assertThat(path).contains("statut[eq]=IRREVOCABLE");
        assertThat(path).contains("payeurAlias[eq]=9b1b3499-3e50-435b-b757-ac7a83d8aa96");

        // Verify sorting (sort=-field for descending)
        assertThat(path).contains("sort=-dateDemande");
    }


    private void mockPagedResponse() {
        String json = TestUtils.loadJson("/unit/mock-responses/paged-demande-paiement-response.json");
        enqueueJson(json, 200);
    }

    private void mockCreateResponse() {
        String json = "{\n" +
                "  \"txId\": \"23511722\",\n" +
                "  \"payeAlias\": \"8b1b2499-3e50-435b-b757-ac7a83d8aa8c\",\n" +
                "  \"payeurAlias\": \"8b1b2499-3e50-435b-b757-ac7a83d8aa7f\",\n" +
                "  \"payeurNom\": \"Nom du client payé\",\n" +
                "  \"payeurPays\": \"BJ\",\n" +
                "  \"montant\": 150000,\n" +
                "  \"categorie\": 500,\n" +
                "  \"dateLimitePaiement\": \"2023-02-22T15:32:01.250Z\",\n" +
                "  \"end2endId\": \"ESNB00120230221153001KXvozkpNvhUk9a\",\n" +
                "  \"dateDemande\": \"2023-02-21T15:30:01.150Z\",\n" +
                "  \"dateEnvoi\": \"2023-02-21T15:30:01.250Z\",\n" +
                "  \"statut\": \"ENVOYE\",\n" +
                "  \"confirmation\": false\n" +
                "}";
        enqueueJson(json, 201);
    }

    private void mockGetResponse() {
        String json = "{\"txId\":\"23511722\",\"payeurNom\":\"Nom du client payé\",\"statut\":\"ENVOYE\"}";
        enqueueJson(json, 200);
    }

    private void mockConfirmResponse() {
        String json = "{\"txId\":\"23552722\",\"confirmation\":true,\"statut\":\"ENVOYE\"}";
        enqueueJson(json, 200);
    }

    private void mockSendDecisionResponse() {
        String json = "{\"txId\":\"23552722\",\"statut\":\"ENVOYE\",\"confirmation\":true}";
        enqueueJson(json, 200);
    }

    private void enqueueJson(String json, int code) {
        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(code));
    }
}
