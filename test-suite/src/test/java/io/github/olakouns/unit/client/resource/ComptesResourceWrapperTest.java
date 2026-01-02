package io.github.olakouns.unit.client.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.olakouns.TestUtils;
import io.github.olakouns.provider.JacksonProvider;
import io.github.olakouns.representation.CompteRepresentation;
import io.github.olakouns.representation.PagedResponse;
import io.github.olakouns.resource.ComptesResource;
import io.github.olakouns.resource.wrapper.AliasResourceWrapper;
import io.github.olakouns.resource.wrapper.ComptesResourceWrapper;
import io.github.olakouns.resource.wrapper.TransactionResourceWrapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.http.HttpHeaders;
import org.assertj.core.api.Assertions;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.jupiter.api.*;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URLDecoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@DisplayName("ComptesResourceWrapper Tests")
public class ComptesResourceWrapperTest {

    private static MockWebServer mockServer;
    private static ObjectMapper objectMapper;
    private ComptesResourceWrapper wrapper;
    private Client client;

    @BeforeAll
    static void setup() throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
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

    @BeforeEach
    void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();

        String baseUrl = mockServer.url("/").toString();
        client = ClientBuilder.newClient().register(JacksonProvider.class);
        WebTarget rootTarget = client.target(baseUrl);

        ComptesResource proxy = ((ResteasyWebTarget) rootTarget).proxy(ComptesResource.class);

        wrapper = new ComptesResourceWrapper(proxy, rootTarget.path("/comptes"));
    }

    

    @Test
    @DisplayName("Should call findByNumero via Proxy")
    void testFindByNumero() throws Exception {
        // Arrange
        CompteRepresentation mockCompte = new CompteRepresentation();
        mockCompte.setNumero("ACC-123");

        mockServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockCompte))
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

        // Act
        CompteRepresentation result = wrapper.findByNumero("ACC-123");

        // Assert
        assertThat(result.getNumero()).isEqualTo("ACC-123");

        // VERIFY:
        RecordedRequest recordedRequest = mockServer.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(recordedRequest.getPath()).isEqualTo("/ACC-123");
    }

    @Test
    @DisplayName("Should return TransactionResourceWrapper and handle singleton correctly")
    void testTransactions() {
        // Act
        TransactionResourceWrapper transactionWrapper1 = wrapper.transactions();
        TransactionResourceWrapper transactionWrapper2 = wrapper.transactions();

        // Assert
        assertThat(transactionWrapper1).isNotNull();
        //Verify singleton behavior
        assertThat(transactionWrapper1).isSameAs(transactionWrapper2);
    }

    @Test
    @DisplayName("Should call alias URL with resolved template")
    void testAliasPathResolution() throws Exception {
        // Arrange: Define test data and mock a simple JSON response
        String accountNumber = "ACC-123";
        mockServer.enqueue(new MockResponse()
                .setBody("{}")
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

        // Act: Get the alias wrapper and trigger a dummy call to verify the path
        AliasResourceWrapper aliasWrapper = wrapper.alias(accountNumber);

        // Assuming AliasResourceWrapper has a list method (inherited from BaseWrapper)
        aliasWrapper.list("0", 10);

        // Assert: Capture the HTTP request sent to the mock server
        RecordedRequest recordedRequest = mockServer.takeRequest();

        // Assert: Verify that the {numero} template was correctly replaced by the real value
        // The expected path should be /comptes/ACC-123/alias
        assertThat(recordedRequest.getPath()).contains( accountNumber + "/alias");
        assertThat(recordedRequest.getMethod()).isEqualTo(HttpMethod.GET);
    }

    @Test
    @DisplayName("Should list comptes with pagination")
    void shouldListAccountsWithPagination() throws Exception {
        // Arrange
        mockComptesListResponse();

        // Act
        PagedResponse<CompteRepresentation> response = wrapper.list("1", 3);

        // Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getData()).hasSize(3);
        Assertions.assertThat(response.getMeta().getPage()).isEqualTo("1");
        Assertions.assertThat(response.getMeta().getSize()).isEqualTo(3);

        // Verify
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertThat(request.getPath()).contains("page=1");
        Assertions.assertThat(request.getPath()).contains("size=3");
        Assertions.assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
    }

    @Test
    @DisplayName("Should query accounts with filters and sorting")
    void shouldQueryAccountsWithFilters() throws Exception {
        // Arrange: Prepare mock response
        mockComptesListResponse();

        // Act: Use the fluent query builder inherited from BaseWrapper
        PagedResponse<CompteRepresentation> response = wrapper.query()
                .page("0")
                .size(20)
                .filter(f -> f
                        .eq("type", "COURANT")
                )
                .sortDesc("dateCreation")
                .execute();

        // Assert
        assertThat(response).isNotNull();

        // Verify: Capture and decode the request path to check filters
        RecordedRequest request = mockServer.takeRequest();
        assertNotNull(request.getPath());
        String path = URLDecoder.decode(request.getPath(), "UTF-8");

        assertThat(path).contains("page=0");
        assertThat(path).contains("size=20");
        assertThat(path).contains("type[eq]=COURANT");
        assertThat(path).contains("sort=-dateCreation");
    }


    private void mockComptesListResponse() {
        String json = TestUtils.loadJson("/unit/mock-responses/paged-compte-response.json");

        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(200));
    }
}
