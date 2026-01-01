package io.github.razacki.unit.client.resource;

import io.github.razacki.TestUtils;
import io.github.razacki.exception.PiSpiException;
import io.github.razacki.provider.JacksonProvider;
import io.github.razacki.representation.PagedResponse;
import io.github.razacki.representation.WebhookRepresentation;
import io.github.razacki.representation.WebhookRequest;
import io.github.razacki.representation.enums.PiWebhookEvent;
import io.github.razacki.resource.WebhookResource;
import io.github.razacki.resource.wrapper.WebhookResourceWrapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.http.HttpHeaders;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.jupiter.api.*;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Tag("unit")
@DisplayName("WebhookResourceWrapper Tests")
public class WebhookResourceWrapperTest {
    private MockWebServer mockServer;
    private Client client;
    private WebhookResourceWrapper wrapper;
    private WebhookResource proxy;


    @BeforeEach
    void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();

        client = ClientBuilder.newClient().register(JacksonProvider.class);
        WebTarget target = client.target(mockServer.url("/webhooks").toString());

        proxy = ((ResteasyWebTarget) target).proxy(WebhookResource.class);

        wrapper = new WebhookResourceWrapper(proxy, target);
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
    @DisplayName("Should return the internal proxy instance")
    void shouldReturnProxy() {
        // Arrange
        // We already have the 'proxy' mock initialized in setUp

        // Act: Call the proxy() method
        WebhookResource returnedProxy = wrapper.proxy();

        // Assert
        assertThat(returnedProxy).isNotNull();
        assertThat(returnedProxy).isSameAs(proxy);
    }

    @Test
    @DisplayName("Should list webhooks with pagination")
    void shouldListWebhooksWithPagination() throws Exception {
        // Arrange
        mockWebhookListResponse();

        // Act
        PagedResponse<WebhookRepresentation> response = wrapper.list("0", 20);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getData()).hasSize(3);
        assertThat(response.getMeta().getPage()).isEqualTo("0");
        assertThat(response.getMeta().getSize()).isEqualTo(20);

        // Verify
        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getPath()).contains("page=0");
        assertThat(request.getPath()).contains("size=20");
        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
    }

    @Test
    @DisplayName("Should query webhooks with filters")
    void shouldQueryWebhooksWithFilters() throws Exception {
        // Arrange
        mockWebhookListResponse();

        // Act
        PagedResponse<WebhookRepresentation> response = wrapper.query()
                .page("0")
                .size(20)
                .filter(f -> f.eq("actif", "true")
                )
                .sortDesc("dateCreation")
                .execute();

        // Assert
        assertThat(response).isNotNull();

        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        String path = URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name());
        assertThat(path).contains("page=0");
        assertThat(path).contains("size=20");
        assertThat(path).contains("actif[eq]=true");
        assertThat(path).contains("sort=-dateCreation");
    }


    @Test
    @DisplayName("Should create webhook successfully")
    void shouldCreateWebhookSuccessfully() throws Exception {
        // Arrange
        WebhookRequest request = createValidWebhookRequest();
        mockWebhookCreateResponse();

        // Act
        WebhookRepresentation result = wrapper.create(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("12345-7785-zrr5r5v-rf5v5f-r5ffc55a");
        assertThat(result.getCallbackUrl()).isEqualTo("https://businessmerchant.com/api/piz/webhooks/");

        // Vérifier la requête HTTP
        RecordedRequest httpRequest = mockServer.takeRequest();
        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(httpRequest.getHeader(HttpHeaders.CONTENT_TYPE)).contains(MediaType.APPLICATION_JSON);
    }

    @Test
    @DisplayName("Should throw exception when creating with null request")
    void shouldThrowExceptionWhenCreatingWithNullRequest() {
        // Act & Assert
        assertThatThrownBy(() -> wrapper.create(null))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("WebhookRequest cannot be null");
    }

    @Test
    @DisplayName("Should find webhook by ID")
    void shouldFindWebhookById() throws Exception {
        // Arrange
        mockWebhookGetResponse();

        // Act
        WebhookRepresentation result = wrapper.findById("webhook-123");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("webhook-123");

        // Vérifier la requête
        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getPath()).contains("/webhook-123");
        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
    }

    @Test
    @DisplayName("Should throw exception when finding with null ID")
    void shouldThrowExceptionWhenFindingWithNullId() {
        assertThatThrownBy(() -> wrapper.findById(null))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("Webhook ID cannot be null or empty");
    }

    @Test
    @DisplayName("Should throw exception when finding with empty ID")
    void shouldThrowExceptionWhenFindingWithEmptyId() {
        assertThatThrownBy(() -> wrapper.findById(""))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("Webhook ID cannot be null or empty");

        assertThatThrownBy(() -> wrapper.findById("   "))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("Webhook ID cannot be null or empty");
    }

    @Test
    @DisplayName("Should update webhook successfully")
    void shouldUpdateWebhookSuccessfully() throws Exception {
        // Arrange
        WebhookRequest request = createValidWebhookRequest();
        mockWebhookUpdateResponse();

        // Act
        WebhookRepresentation result = wrapper.update("webhook-123", request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("webhook-123");

        // Vérifier la requête
        RecordedRequest httpRequest = mockServer.takeRequest();
        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.PUT);
        assertThat(httpRequest.getPath()).contains("/webhook-123");
        assertThat(httpRequest.getHeader(HttpHeaders.CONTENT_TYPE)).contains(MediaType.APPLICATION_JSON);
    }

    @Test
    @DisplayName("Should throw exception when updating with null ID")
    void shouldThrowExceptionWhenUpdatingWithNullId() {
        WebhookRequest request = createValidWebhookRequest();

        assertThatThrownBy(() -> wrapper.update(null, request))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("Webhook ID cannot be null or empty");
    }

    @Test
    @DisplayName("Should throw exception when updating with empty ID")
    void shouldThrowExceptionWhenUpdatingWithEmptyId() {
        WebhookRequest request = createValidWebhookRequest();

        assertThatThrownBy(() -> wrapper.update("", request))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("Webhook ID cannot be null or empty");
    }

    @Test
    @DisplayName("Should throw exception when updating with null request")
    void shouldThrowExceptionWhenUpdatingWithNullRequest() {
        assertThatThrownBy(() -> wrapper.update("webhook-123", null))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("WebhookRequest cannot be null");
    }

    @Test
    @DisplayName("Should delete webhook successfully")
    void shouldDeleteWebhookSuccessfully() throws Exception {
        // Arrange
        mockWebhookDeleteResponse();

        // Act
        wrapper.delete("webhook-123");

        // Assert - Vérifier la requête
        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo(HttpMethod.DELETE);
        assertThat(request.getPath()).contains("/webhook-123");
    }

    @Test
    @DisplayName("Should throw exception when deleting with null ID")
    void shouldThrowExceptionWhenDeletingWithNullId() {
        assertThatThrownBy(() -> wrapper.delete(null))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("Webhook ID cannot be null or empty");
    }

    @Test
    @DisplayName("Should throw exception when deleting with empty ID")
    void shouldThrowExceptionWhenDeletingWithEmptyId() {
        assertThatThrownBy(() -> wrapper.delete(""))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("Webhook ID cannot be null or empty");
    }

    @Test
    @DisplayName("Should renew webhook secret successfully")
    void shouldRenewWebhookSecretSuccessfully() throws Exception {
        // Arrange
        mockWebhookRenewResponse();

        // Act
        WebhookRepresentation result = wrapper.renewSecret("webhook-123", OffsetDateTime.now().plusDays(1));

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("webhook-123");
        assertThat(result.getSecret()).isNotEmpty();

        // Vérifier la requête
        RecordedRequest httpRequest = mockServer.takeRequest();
        assertThat(httpRequest.getMethod()).isEqualTo(HttpMethod.POST);
        assertThat(httpRequest.getPath()).contains("/webhook-123/secrets");
    }

    @Test
    @DisplayName("Should throw exception when renewing secret with null ID")
    void shouldThrowExceptionWhenRenewingSecretWithNullId() {
        assertThatThrownBy(() -> wrapper.renewSecret(null, OffsetDateTime.now().plusDays(1)))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("Webhook ID cannot be null or empty");
    }

    @Test
    @DisplayName("Should throw exception when renewing secret with null request")
    void shouldThrowExceptionWhenRenewingSecretWithNullRequest() {
        assertThatThrownBy(() -> wrapper.renewSecret("webhook-123", null))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("DateExpiration cannot be null");
    }

    private WebhookRequest createValidWebhookRequest() {
        WebhookRequest request = new WebhookRequest();
        request.setCallbackUrl("https://businessmerchant.com/api/piz/webhooks/");
        request.setEvents(Arrays.asList(PiWebhookEvent.RTP_RECU, PiWebhookEvent.RTP_REJETE));
        return request;
    }

    private void mockWebhookListResponse() {
        String json = TestUtils.loadJson("/unit/mock-responses/paged-webhooks-response.json");

        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(200));
    }

    private void mockWebhookCreateResponse() {
        String json = TestUtils.loadJson("/unit/mock-responses/webhooks-create-response.json");

        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(201));
    }

    private void mockWebhookGetResponse() {
        String json = "{\n" +
                "  \"id\": \"webhook-123\",\n" +
                "  \"callbackUrl\": \"https://example.com/webhook\",\n" +
                "  \"events\": [\"RETOUR_ENVOYE\"],\n" +
                "  \"dateCreation\": \"2024-12-24T10:00:00Z\"\n" +
                "}";

        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(200));
    }

    private void mockWebhookUpdateResponse() {
        String json = "{\n" +
                "  \"id\": \"webhook-123\",\n" +
                "  \"callbackUrl\": \"https://example.com/webhook-updated\",\n" +
                "  \"alias\": \"alias-updated\",\n" +
                "  \"events\": [\"RETOUR_ENVOYE\", \"ANNULATION_DEMANDE\"],\n" +
                "  \"actif\": true,\n" +
                "  \"dateModification\": \"2024-12-24T11:00:00Z\"\n" +
                "}";

        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(200));
    }

    private void mockWebhookDeleteResponse() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(204));
    }

    private void mockWebhookRenewResponse() {
        String json = "{\n" +
                "  \"id\": \"webhook-123\",\n" +
                "  \"callbackUrl\": \"https://example.com/webhook\",\n" +
                "  \"events\": [\"RTP_RECU\"],\n" +
                "  \"secret\": \"secret_new_xyz789\",\n" +
                "  \"actif\": true,\n" +
                "  \"dateModification\": \"2024-12-24T12:00:00Z\"\n" +
                "}";

        mockServer.enqueue(new MockResponse()
                .setBody(json)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setResponseCode(200));
    }


}
