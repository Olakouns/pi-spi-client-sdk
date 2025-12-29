package io.github.razacki.unit.client.resource;

import io.github.razacki.TestUtils;
import io.github.razacki.exception.PiSpiException;
import io.github.razacki.provider.JacksonProvider;
import io.github.razacki.representation.PagedResponse;
import io.github.razacki.representation.PaiementRepresentation;
import io.github.razacki.representation.enums.PaiementAnnulationMotif;
import io.github.razacki.representation.enums.PaiementAnnulationStatut;
import io.github.razacki.representation.enums.PaiementsStatut;
import io.github.razacki.resource.PaiementResource;
import io.github.razacki.resource.wrapper.PaiementResourceWrapper;
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
@DisplayName("PaiementResourceWrapper Tests")
public class PaiementResourceWrapperTest {
    private MockWebServer mockServer;
    private PaiementResourceWrapper wrapper;
    private Client client;

    @BeforeEach
    void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();

        client = ClientBuilder.newClient().register(JacksonProvider.class);
        WebTarget rootTarget = client.target(mockServer.url("/paiements").toString());

        PaiementResource proxy = ((ResteasyWebTarget) rootTarget).proxy(PaiementResource.class);

        wrapper = new PaiementResourceWrapper(proxy, rootTarget);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (client != null) client.close();
        if (mockServer != null) mockServer.shutdown();
    }

    @Test
    @DisplayName("Should list payments with pagination")
    void shouldListPayments() throws Exception {
        // Arrange
        enqueueJson(TestUtils.loadJson("/unit/mock-responses/paged-paiement-response.json"), 200);

        // Act
        PagedResponse<PaiementRepresentation> response = wrapper.list("0", 10);

        // Assert
        assertThat(response.getData()).hasSize(2);
        assertThat(response.getData().get(0).getTxId()).isEqualTo("a755485465e4d5ddsqddqde");

        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(request.getPath()).contains("page=0").contains("size=10");
    }

    @Test
    @DisplayName("Should query payments with complex filters")
    void shouldQueryPaymentsWithFilters() throws Exception {
        // Arrange
        enqueueJson(TestUtils.loadJson("/unit/mock-responses/paged-paiement-response.json"), 200);

        // Act
        PagedResponse<PaiementRepresentation> response = wrapper.query()
                .page("1")
                .size(20)
                .filter(f -> f
                        .eq("statut", PaiementsStatut.IRREVOCABLE)
                        .gt("montant", 5000)
                        .sortDesc("dateEnvoi")
                )
                .execute();

        // Assert
        assertThat(response).isNotNull();

        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        String path = URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name());

        // Vérification des paramètres de requête générés par le FilterBuilder
        assertThat(path).contains("page=1");
        assertThat(path).contains("size=20");
        assertThat(path).contains("statut[eq]=IRREVOCABLE");
        assertThat(path).contains("montant[gt]=5000");
        assertThat(path).contains("sort=-dateEnvoi");
    }

    @Test
    @DisplayName("Should get payment status by end2endId")
    void shouldGetStatus() throws Exception {
        // Arrange
        String end2endId = "ESNB00120230221103000KXvozkpNvhUk9e";
        enqueueJson(TestUtils.loadJson("/unit/mock-responses/paiement-status-response.json"), 200);

        // Act
        PaiementRepresentation result = wrapper.getStatus(end2endId);

        // Assert
        assertThat(result.getEnd2endId()).isEqualTo(end2endId);
        assertThat(result.getCategorie().getCode()).isEqualTo("500");
        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(request.getPath()).contains("/" + end2endId + "/statuts");
    }

    @Test
    @DisplayName("Should create a payment request (HTTP 200)")
    void shouldCreatePayment() throws Exception {
        // Arrange
        enqueueJson(TestUtils.loadJson("/unit/mock-responses/create-paiement-response.json"), 200);
        PaiementRepresentation requestBody = PaiementRepresentation.builder()
                .txId("23552722")
                .payeurAlias("8b1b2499-3e50-435b-b757-ac7a83d8aa7f")
                .payeAlias("9b1b2499-3e50-435b-b757-ac7a83d8aa8c")
                .montant(BigDecimal.valueOf(150000))
                .confirmation(false)
                .build();

        // Act
        PaiementRepresentation result = wrapper.create(requestBody);

        // Assert
        assertThat(result.getMontant().doubleValue()).isEqualTo(150000);
        assertThat(result.getStatut()).isEqualTo(PaiementsStatut.ENVOYE);
        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(request.getHeader(HttpHeaders.CONTENT_TYPE)).contains(MediaType.APPLICATION_JSON);
    }

    @Test
    @DisplayName("Should find payment by technical txId")
    void shouldFindById() throws Exception {
        // Arrange
        String txId = "Tag-a7d96dfdsdf965dfdsdf";
        enqueueJson(TestUtils.loadJson("/unit/mock-responses/paiement-status-response.json"), 200);

        // Act
        PaiementRepresentation result = wrapper.findById(txId);

        // Assert
        assertThat(result.getTxId()).isEqualTo(txId);

        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(request.getPath()).endsWith("/" + txId);
    }

    @Test
    @DisplayName("Should confirm payment with decision object")
    void shouldConfirmPayment() throws Exception {
        // Arrange
        String txId = "23552722";
        enqueueJson(TestUtils.loadJson("/unit/mock-responses/create-paiement-response.json"), 200);

        // Act
        PaiementRepresentation result = wrapper.confirm(txId, true);

        // Assert
        assertThat(result).isNotNull();

        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo(HttpMethod.PUT);
        assertThat(request.getPath()).contains("/" + txId);
        assertThat(request.getBody().readUtf8()).contains("\"decision\":true");
    }

    @Test
    @DisplayName("Should initiate return of funds (POST /paiements/{id}/retours)")
    void shouldReturnFunds() throws Exception {
        // Arrange
        String end2endId = "ESNB00120230221103000KXvozkpNvhUk9e";
        enqueueJson(TestUtils.loadJson("/unit/mock-responses/paiement-status-response.json"), 200);

        // Act
        PaiementRepresentation result = wrapper.returnFunds(end2endId);

        // Assert
        assertThat(result).isNotNull();
        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo(HttpMethod.PUT);
        assertThat(request.getPath()).contains("/" + end2endId + "/retours");
    }

    @Test
    @DisplayName("Should request cancellation with specific motif")
    void shouldRequestCancellation() throws Exception {
        // Arrange
        String end2endId = "ESNB00120230221103000KXvozkpNvhUk9e";
        enqueueJson(TestUtils.loadJson("/unit/mock-responses/paiement-annulation-response.json"), 200);

        // Act
        PaiementRepresentation result = wrapper.requestCancellation(end2endId, PaiementAnnulationMotif.DUPL);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCategorie().getCode()).isEqualTo("401");
        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(request.getPath()).contains("/" + end2endId + "/annulations");

        // Verify
        String body = request.getBody().readUtf8();
        assertThat(body).contains("\"raison\":\"DUPL\"");
    }

    @Test
    @DisplayName("Should respond to cancellation with a decision (Accept/Reject)")
    void shouldRespondToCancellation() throws Exception {
        // Arrange
        String end2endId = "ESNB00120230221103000KXvozkpNvhUk9e";
        enqueueJson(TestUtils.loadJson("/unit/mock-responses/paiement-annulation-response.json"), 200);

        // Act
        PaiementRepresentation result = wrapper.respondToCancellation(end2endId, true);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getAnnulationStatut()).isEqualTo(PaiementAnnulationStatut.ENVOYE);
        assertThat(result.getDateConfirmation().toString()).contains("2023-02-21T10:30:00.050Z");
        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo(HttpMethod.PUT);
        assertThat(request.getPath()).contains("/" + end2endId + "/annulations/reponses");

        // Verify
        String body = request.getBody().readUtf8();
        assertThat(body).contains("\"decision\":true");
    }

    @Test
    @DisplayName("Should fail when end2endId is null or empty for returnFunds")
    void shouldValidateEnd2EndIdOnReturn() {
        // Test with null
        assertThatThrownBy(() -> wrapper.returnFunds(null))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("PaiementRepresentation end2endId cannot be null or empty");

        // Test awith empty string
        assertThatThrownBy(() -> wrapper.returnFunds(""))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("PaiementRepresentation end2endId cannot be null or empty");
    }


    @Test
    @DisplayName("Should throw exception when creating with null body")
    void shouldValidateNullCreate() {
        assertThatThrownBy(() -> wrapper.create(null))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("paiementRepresentation cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when txId is empty on findById")
    void shouldValidateEmptyTxId() {
        assertThatThrownBy(() -> wrapper.findById("  "))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("PaiementRepresentation txId cannot be null or empty");
    }


    private void enqueueJson(String json, int code) {
        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(code));
    }
}
