package io.github.razacki.unit.client.resource;

import io.github.razacki.TestUtils;
import io.github.razacki.exception.PiSpiException;
import io.github.razacki.provider.JacksonProvider;
import io.github.razacki.representation.PagedResponse;
import io.github.razacki.representation.TransactionRepresentation;
import io.github.razacki.representation.enums.TransactionStatut;
import io.github.razacki.resource.TransactionsResource;
import io.github.razacki.resource.wrapper.TransactionResourceWrapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@Tag("unit")
@DisplayName("TransactionResourceWrapper Tests")
public class TransactionResourceWrapperTest {
    private MockWebServer mockServer;
    private Client client;
    private TransactionResourceWrapper wrapper;


    @BeforeEach
    void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();

        client = ClientBuilder.newClient().register(JacksonProvider.class);
        WebTarget target = client.target(mockServer.url("/comptes/transactions").toString());

        TransactionsResource proxy = ((ResteasyWebTarget) target).proxy(TransactionsResource.class);

        wrapper = new TransactionResourceWrapper(proxy, target);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (client != null) {
            client.close();
        }
        if (mockServer != null) {
            mockServer.shutdown();
        }
    }

    @Test
    @DisplayName("Should list transactions with pagination")
    void shouldListTransactionsWithPagination() throws Exception {
        // Arrange
        mockTransactionListResponse();

        // Act
        PagedResponse<TransactionRepresentation> response = wrapper.list("1", 10);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData()).hasSize(2);

        // Verify the first transaction
        TransactionRepresentation firstTx = response.getData().get(0);
        assertThat(firstTx.getTxId()).isEqualTo("23511722");
        assertThat(firstTx.getPayeurNumero()).isEqualTo("ac7a83d8aa7f");
        assertThat(firstTx.getPayeNumero()).isEqualTo("jdko7588578");
        assertThat(firstTx.getMontant().doubleValue()).isEqualTo(150000);
        assertThat(firstTx.getStatut()).isEqualTo(TransactionStatut.INITIE);
        assertThat(firstTx.getDateEnvoi()).isNotNull();

        // Verify the second transaction
        TransactionRepresentation secondTx = response.getData().get(1);
        assertThat(secondTx.getStatut()).isEqualTo(TransactionStatut.IRREVOCABLE);
        assertThat(secondTx.getDateIrrevocabilite()).isNotNull();

        // Verify pagination metadata
        assertThat(response.getMeta()).isNotNull();
        assertThat(response.getMeta().getPage()).isEqualTo("1");
        assertThat(response.getMeta().getLimit()).isEqualTo(10);
        assertThat(response.getMeta().getSize()).isEqualTo(2);

        // Verify the request made to the mock server
        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getPath()).contains("page=1");
        assertThat(request.getPath()).contains("size=10");
        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
    }

    @Test
    @DisplayName("Should query transactions with filters")
    void shouldQueryTransactionsWithFilters() throws Exception {
        // Arrange
        mockTransactionListResponse();

        // Act
        PagedResponse<TransactionRepresentation> response = wrapper.query()
                .page("1")
                .size(10)
                .filter(f -> f
                        .eq("statut", "INITIE")
                        .gte("montant", "100000")
                        .sortDesc("dateEnvoi")
                )
                .execute();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getData()).hasSize(2);

        // Verify the request made to the mock server
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        String path = URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name());
        assertThat(path).contains("page=1");
        assertThat(path).contains("size=10");
        assertThat(path).contains("statut[eq]=INITIE");
        assertThat(path).contains("montant[gte]=100000");
        assertThat(path).contains("sort=-dateEnvoi");
    }


    @Test
    @DisplayName("Should query transactions by date range")
    void shouldQueryTransactionsByDateRange() throws Exception {
        // Arrange
        mockTransactionListResponse();

        // Act
        PagedResponse<TransactionRepresentation> response = wrapper.query()
                .page("0")
                .size(20)
                .filter(f -> f
                        .between("dateEnvoi", "2023-02-01T00:00:00Z", "2023-02-28T23:59:59Z")
                        .eq("statut", "IRREVOCABLE")
                )
                .execute();

        // Assert
        assertThat(response).isNotNull();

        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        String path = URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name());
        assertThat(path).contains("dateEnvoi[gte]=2023-02-01T00:00:00Z");
        assertThat(path).contains("dateEnvoi[lte]=2023-02-28T23:59:59Z");
        assertThat(path).contains("statut[eq]=IRREVOCABLE");
    }

    @Test
    @DisplayName("Should create transaction successfully")
    void shouldCreateTransactionSuccessfully() throws Exception {
        // Arrange
        TransactionRepresentation request = createValidTransactionRequest();
        mockTransactionCreateResponse();

        // Act
        TransactionRepresentation result = wrapper.create(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTxId()).isEqualTo("23511722");
        assertThat(result.getPayeurNumero()).isEqualTo("ac7a83d8aa7f");
        assertThat(result.getPayeNumero()).isEqualTo("jdko7588578");
        assertThat(result.getMontant().doubleValue()).isEqualTo(150000);
        assertThat(result.getStatut()).isEqualTo(TransactionStatut.INITIE);
        assertThat(result.getDateEnvoi()).isNotNull();
        assertThat(result.getEnd2endId()).isEqualTo("ESNB00120230221153554KXvozkpNvhUk9e");

        // Verify the request made to the mock server
        RecordedRequest httpRequest = mockServer.takeRequest();
        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(httpRequest.getHeader(HttpHeaders.CONTENT_TYPE)).contains(MediaType.APPLICATION_JSON);

        // Verify request body
        String requestBody = httpRequest.getBody().readUtf8();
        assertThat(requestBody).contains("\"txId\":\"123511722\"");
        assertThat(requestBody).contains("\"payeurNumero\":\"ac7a83d8aa7f\"");
        assertThat(requestBody).contains("\"payeNumero\":\"jdko7588578\"");
        assertThat(requestBody).contains("\"montant\":150000");
    }

    @Test
    @DisplayName("Should create transaction with null txId (auto-generated)")
    void shouldCreateTransactionWithNullTxId() {
        // Arrange
        TransactionRepresentation request = createValidTransactionRequest();
        request.setTxId(null); // TxId will be auto-generated
        mockTransactionCreateResponse();

        // Act
        TransactionRepresentation result = wrapper.create(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTxId()).isNotNull(); // Auto-generated txId
        assertThat(result.getTxId()).isEqualTo("23511722");
    }

    @Test
    @DisplayName("Should create transaction and verify statut is INITIE")
    void shouldCreateTransactionAndVerifyStatutInitie() throws Exception {
        // Arrange
        TransactionRepresentation request = createValidTransactionRequest();
        mockTransactionCreateResponse();

        // Act
        TransactionRepresentation result = wrapper.create(request);

        // Assert
        assertThat(result.getStatut()).isEqualTo(TransactionStatut.INITIE);
        assertThat(result.getDateEnvoi()).isNotNull();
        assertThat(result.getDateIrrevocabilite()).isNull(); // Pas encore irréversible
    }


    @Test
    @DisplayName("Should throw exception when creating with null request")
    void shouldThrowExceptionWhenCreatingWithNullRequest() {
        // Act & Assert
        assertThatThrownBy(() -> wrapper.create(null))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("cannot be null");
    }

    @Test
    @DisplayName("Should create transaction with minimum required fields")
    void shouldCreateTransactionWithMinimumFields() throws Exception {
        // Arrange
        TransactionRepresentation request = new TransactionRepresentation();
        request.setPayeurNumero("ac7a83d8aa7f");
        request.setPayeNumero("jdko7588578");
        request.setMontant(BigDecimal.valueOf(150000));
        mockTransactionCreateResponse();

        // Act
        TransactionRepresentation result = wrapper.create(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTxId()).isNotNull();
    }

    @Test
    @DisplayName("Should correctly parse dates with milliseconds")
    void shouldCorrectlyParseDatesWithMilliseconds() {
        // Arrange
        mockTransactionListResponse();

        // Act
        PagedResponse<TransactionRepresentation> response = wrapper.list("1", 10);

        // Assert
        TransactionRepresentation tx = response.getData().get(0);
        assertThat(tx.getDateEnvoi()).isNotNull();
        assertThat(tx.getDateEnvoi().toString()).contains("2023-02-21T15:30:01.250");
    }

    @Test
    @DisplayName("Should correctly parse dateIrrevocabilite for IRREVOCABLE status")
    void shouldCorrectlyParseDateIrrevocabilite() throws Exception {
        // Arrange
        mockTransactionListResponse();

        // Act
        PagedResponse<TransactionRepresentation> response = wrapper.list("1", 10);

        // Assert
        TransactionRepresentation irrevocableTx = response.getData().get(1);
        assertThat(irrevocableTx.getStatut()).isEqualTo(TransactionStatut.IRREVOCABLE);
        assertThat(irrevocableTx.getDateIrrevocabilite()).isNotNull();
        assertThat(irrevocableTx.getDateIrrevocabilite().toString()).contains("2023-02-21T15:30:01.500");
    }

    @Test
    @DisplayName("Should handle different transaction statuses")
    void shouldHandleDifferentTransactionStatuses() throws Exception {
        // Arrange
        mockTransactionListResponse();

        // Act
        PagedResponse<TransactionRepresentation> response = wrapper.list("1", 10);

        // Assert
        assertThat(response.getData())
                .extracting(TransactionRepresentation::getStatut)
                .containsExactly(TransactionStatut.INITIE, TransactionStatut.IRREVOCABLE);
    }

    @Test
    @DisplayName("Should filter transactions by status")
    void shouldFilterTransactionsByStatus() throws Exception {
        // Arrange
        mockTransactionListResponse();

        // Act
        PagedResponse<TransactionRepresentation> response = wrapper.query()
                .page("0")
                .size(10)
                .filter(f -> f.in("statut", "INITIE", "IRREVOCABLE", "COMPLETE"))
                .execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        String path = URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name());
        assertThat(path).contains("statut[in]=INITIE,IRREVOCABLE,COMPLETE");
    }




    private void mockTransactionListResponse() {
        String json = TestUtils.loadJson("/unit/mock-responses/paged-transactions-response.json");

        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(200));
    }

    private void mockTransactionCreateResponse() {
        String json = "{\n" +
                "  \"txId\": \"23511722\",\n" +
                "  \"payeurNumero\": \"ac7a83d8aa7f\",\n" +
                "  \"payeNumero\": \"jdko7588578\",\n" +
                "  \"montant\": 150000,\n" +
                "  \"statut\": \"INITIE\",\n" +
                "  \"dateEnvoi\": \"2023-02-21T15:30:01.250Z\",\n" +
                "  \"end2endId\": \"ESNB00120230221153554KXvozkpNvhUk9e\"\n" +
                "}";

        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(201));
    }


    private TransactionRepresentation createValidTransactionRequest() {
        TransactionRepresentation request = new TransactionRepresentation();
        request.setTxId("123511722");
        request.setPayeurNumero("ac7a83d8aa7f");
        request.setPayeNumero("jdko7588578");
        request.setMontant(BigDecimal.valueOf(150000));
        return request;
    }
}
