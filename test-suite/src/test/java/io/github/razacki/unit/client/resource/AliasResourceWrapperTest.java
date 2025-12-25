package io.github.razacki.unit.client.resource;

import io.github.exception.PiSpiException;
import io.github.provider.JacksonProvider;
import io.github.razacki.TestUtils;
import io.github.representation.AliasRepresentation;
import io.github.representation.CreateAliasRequest;
import io.github.representation.PagedResponse;
import io.github.resource.AliasResource;
import io.github.resource.wrapper.AliasResourceWrapper;
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
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Tag("unit")
@DisplayName("AliasResourceWrapper Tests")
public class AliasResourceWrapperTest {
    private MockWebServer mockServer;
    private Client client;
    private AliasResourceWrapper wrapper;

    // Constant for the resolved account number in tests
    private static final String ACCOUNT_NUM = "ACC-123";

    @BeforeEach
    void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();

        // Register the JacksonProvider to handle Java 8 dates and JSON config
        client = ClientBuilder.newClient().register(JacksonProvider.class);

        WebTarget target = client.target(mockServer.url("/").toString()).path("/comptes/{numero}/alias")
                .resolveTemplate("numero", ACCOUNT_NUM);

        AliasResource proxy = ((ResteasyWebTarget) target).proxy(AliasResource.class);
        wrapper = new AliasResourceWrapper(proxy, target);
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
    @DisplayName("Should list aliases with full resolved path")
    void shouldListAliasesWithPagination() throws Exception {
        // Arrange
        mockPagedAliasesResponse();

        // Act
        PagedResponse<AliasRepresentation> response = wrapper.list(1, 2);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getMeta().getPage()).isEqualTo("1");

        // Verify: Path should contain the account number and alias suffix
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        String path = URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name());
        assertThat(path).contains("/comptes/" + ACCOUNT_NUM + "/alias");
        assertThat(path).contains("page=1");
    }

    @Test
    @DisplayName("Should query aliases using the fluent builder")
    void shouldQueryAliasesWithFilters() throws Exception {
        // Arrange
        mockPagedAliasesResponse();

        // Act
        wrapper.query()
                .page("1")
                .filter(f -> f.eq("type", "SHID"))
                .execute();

        // Verify
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        String path = URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name());
        assertThat(path).contains("/comptes/" + ACCOUNT_NUM + "/alias");
        assertThat(path).contains("type[eq]=SHID");
    }

    @Test
    @DisplayName("Should create alias for specific account")
    void shouldCreateAliasSuccessfully() throws Exception {
        // Arrange
        CreateAliasRequest request = new CreateAliasRequest("SHID");
        mockCreateAliasResponse();

        // Act
        AliasRepresentation result = wrapper.create(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCle()).isEqualTo("8b1b2499-3e50-435b-b757-ac7a83d8aa7f");

        // Verify HTTP request
        RecordedRequest httpRequest = mockServer.takeRequest();
        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(httpRequest.getPath()).startsWith("/comptes/" + ACCOUNT_NUM + "/alias");
    }

    @Test
    @DisplayName("Should throw exception when creating with null request")
    void shouldThrowExceptionWhenCreatingWithNullRequest() {
        assertThatThrownBy(() -> wrapper.create(null))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("CreateAliasRequest cannot be null");
    }


    @Test
    @DisplayName("Should delete alias appending the key to the path")
    void shouldDeleteAliasSuccessfully() throws Exception {
        // Arrange
        String aliasKey = "ALIAS-999";
        mockServer.enqueue(new MockResponse().setResponseCode(204));

        // Act
        wrapper.delete(aliasKey);

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo(HttpMethod.DELETE);

        // Final Path check: Base + Key
        String expectedPath = "/comptes/" + ACCOUNT_NUM + "/alias/" + aliasKey;
        assertThat(request.getPath()).isEqualTo(expectedPath);
    }

    @Test
    @DisplayName("Should validate key before deletion")
    void shouldThrowExceptionWhenDeletingWithInvalidKey() {
        assertThatThrownBy(() -> wrapper.delete(null))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("Alias cle cannot be null or empty");
    }

    private void mockPagedAliasesResponse() {
        String json = TestUtils.loadJson("/unit/mock-responses/paged-alias-response.json");
        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(200));
    }

    private void mockCreateAliasResponse() {
        String json = "{\n" +
                "  \"cle\": \"8b1b2499-3e50-435b-b757-ac7a83d8aa7f\",\n" +
                "  \"type\": \"SHID\",\n" +
                "  \"dateCreation\": \"2023-02-21T15:30:01.250Z\"\n" +
                "}";

        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(201));
    }
}
