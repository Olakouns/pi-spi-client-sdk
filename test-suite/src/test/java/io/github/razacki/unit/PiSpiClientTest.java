package io.github.razacki.unit;

import io.github.BaseConfig;
import io.github.PiSpiClient;
import io.github.filter.ApiKeyFilter;
import io.github.filter.BearerAuthFilter;
import io.github.filter.PiSpiClientResponseFilter;
import io.github.provider.ResteasyClientProvider;
import io.github.resource.ApiResource;
import io.github.resource.wrapper.ApiResourceWrapper;
import io.github.token.TokenService;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.jupiter.api.*;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Tag("unit")
@DisplayName("PiSpiClient Tests")
public class PiSpiClientTest {

    @Mock
    private Client mockClient;

    @Mock(answer = Answers.RETURNS_SELF)
    private ResteasyWebTarget mockTarget;

    @Mock
    private ResteasyClientProvider mockProvider;

    @Mock
    private ApiResource mockApiResource;

    @Mock
    private TokenService mockTokenService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        // Initialise tous les mocks annotés avec une seule instance par variable
        mocks = MockitoAnnotations.openMocks(this);

        // Injecter le provider statique pour éviter l'usage du provider réel
        PiSpiClient.setClientProvider(mockProvider);

        // Configuration de la chaîne : le client renvoie TOUJOURS notre instance de mockTarget
        when(mockClient.target(anyString())).thenReturn(mockTarget);

        // Configuration des proxies pour les ressources
        when(mockProvider.targetProxy(any(WebTarget.class), eq(ApiResource.class)))
                .thenReturn(mockApiResource);
        when(mockProvider.targetProxy(any(WebTarget.class), eq(TokenService.class)))
                .thenReturn(mockTokenService);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    @DisplayName("Should create client with basic config")
    void shouldCreateClientWithBasicConfig() {
        // Arrange
        BaseConfig config = BaseConfig.builder()
                .serverUrl("https://api.pispi.sn")
                .apiKey("test-api-key")
                .build();
        // Act
        PiSpiClient client = PiSpiClient.getInstance(
                config,
                mockClient,
                false,
                null,
                null,
                null
        );

        // Assert
        assertThat(client).isNotNull();
        assertThat(client.isClosed()).isFalse();

        // Verify
        verify(mockClient, atLeastOnce()).target("https://api.pispi.sn");

        verify(mockTarget).register(any(BearerAuthFilter.class));
        verify(mockTarget).register(any(PiSpiClientResponseFilter.class), eq(200));
        verify(mockTarget).register(any(ApiKeyFilter.class));
    }

    @Test
    @DisplayName("Should create client with auth token")
    void shouldCreateClientWithAuthToken() {
        // Arrange
        BaseConfig config = BaseConfig.builder()
                .serverUrl("https://api.pispi.sn")
                .apiKey("test-api-key")
                .build();
        String authToken = "bearer-token-12345";

        // Act
        PiSpiClient client = PiSpiClient.getInstance(
                config,
                mockClient,
                false,
                null,
                null,
                authToken
        );

        // Assert
        assertThat(client).isNotNull();
        assertThat(client.tokenManager()).isNull();

        verify(mockClient, times(1)).target("https://api.pispi.sn");
        verify(mockTarget).register(any(BearerAuthFilter.class));
        verify(mockTarget).register(any(PiSpiClientResponseFilter.class), eq(200));
        verify(mockTarget).register(any(ApiKeyFilter.class));
    }

    @Test
    @DisplayName("Should NOT register API key filter when no API key is provided")
    void shouldNotRegisterApiKeyFilterWhenNoApiKey() {
        // Arrange
        BaseConfig config = BaseConfig.builder()
                .serverUrl("https://api.pispi.sn")
                .build();

        // Act
        PiSpiClient client = PiSpiClient.getInstance(
                config,
                mockClient,
                false,
                null,
                null,
                "token"
        );

        // Assert
        assertThat(client).isNotNull();

        // Verify
        verify(mockTarget).register(any(BearerAuthFilter.class));
        verify(mockTarget).register(any(PiSpiClientResponseFilter.class), anyInt());
        verify(mockTarget, never()).register(any(ApiKeyFilter.class));
    }

    @Test
    @DisplayName("Should create client with SSL context")
    void shouldCreateClientWithSslContext() throws Exception {
        // Arrange
        BaseConfig config = BaseConfig.builder()
                .serverUrl("https://api.pispi.sn")
                .apiKey("test-api-key")
                .build();
        SSLContext sslContext = SSLContext.getDefault();

        // Act
        PiSpiClient client = PiSpiClient.getInstance(
                config,
                mockClient,
                false,
                sslContext,
                null,
                null
        );

        // Assert
        assertThat(client).isNotNull();
    }

    @Test
    @DisplayName("Should create and cache ApiResourceWrapper")
    void shouldCreateAndCacheApiResourceWrapper() {
        // Arrange
        BaseConfig config = BaseConfig.builder()
                .serverUrl("https://api.pispi.sn")
                .apiKey("test-api-key")
                .build();

        PiSpiClient client = PiSpiClient.getInstance(
                config,
                mockClient,
                false,
                null,
                null,
                "token"
        );

        // Act
        ApiResourceWrapper wrapper1 = client.api();
        ApiResourceWrapper wrapper2 = client.api();

        // Assert
        assertThat(wrapper1).isNotNull();
        assertThat(wrapper2).isNotNull();
        assertThat(wrapper1).isSameAs(wrapper2); // Même instance (cached)

        // Verify that targetProxy was called only once for ApiResource
        verify(mockProvider, times(1)).targetProxy(any(WebTarget.class), eq(ApiResource.class));
    }

    @Test
    @DisplayName("Should create ApiResourceWrapper lazily")
    void shouldCreateApiResourceWrapperLazily() {
        // Arrange
        BaseConfig config = BaseConfig.builder()
                .serverUrl("https://api.pispi.sn")
                .apiKey("test-api-key")
                .build();

        // Reset pour ne compter que les appels après la création du client
        reset(mockProvider);

        PiSpiClient client = PiSpiClient.getInstance(
                config,
                mockClient,
                false,
                null,
                null,
                "token"
        );

        // targetProxy should not have been called yet
        verify(mockProvider, never()).targetProxy(any(), eq(ApiResource.class));

        // Act
        ApiResourceWrapper wrapper = client.api();

        // Assert
        assertThat(wrapper).isNotNull();
        verify(mockProvider, times(1)).targetProxy(any(WebTarget.class), eq(ApiResource.class));
    }

    @Test
    @DisplayName("Should have TokenManager when no authToken provided")
    void shouldHaveTokenManagerWhenNoAuthToken() {
        // Arrange
        BaseConfig config = BaseConfig.builder()
                .serverUrl("https://api.pispi.sn")
                .clientId("client-123")
                .clientSecret("secret-456")
                .apiKey("test-api-key")
                .build();

        // Act
        PiSpiClient client = PiSpiClient.getInstance(
                config,
                mockClient,
                false,
                null,
                null,
                null
        );

        // Assert
        assertThat(client.tokenManager()).isNotNull();
    }

    @Test
    @DisplayName("Should NOT have TokenManager when authToken provided")
    void shouldNotHaveTokenManagerWhenAuthTokenProvided() {
        // Arrange
        BaseConfig config = BaseConfig.builder()
                .serverUrl("https://api.pispi.sn")
                .apiKey("test-api-key")
                .build();
        String authToken = "bearer-token-12345";

        // Act
        PiSpiClient client = PiSpiClient.getInstance(
                config,
                mockClient,
                false,
                null,
                null,
                authToken
        );

        // Assert
        assertThat(client.tokenManager()).isNull();
    }

    @Test
    @DisplayName("Should close client successfully")
    void shouldCloseClientSuccessfully() throws Exception {
        // Arrange
        BaseConfig config = BaseConfig.builder()
                .serverUrl("https://api.pispi.sn")
                .apiKey("test-api-key")
                .build();

        PiSpiClient client = PiSpiClient.getInstance(
                config,
                mockClient,
                false,
                null,
                null,
                "token"
        );

        assertThat(client.isClosed()).isFalse();

        // Act
        client.close();

        // Assert
        assertThat(client.isClosed()).isTrue();
        verify(mockClient).close();
    }

    @Test
    @DisplayName("Should be closeable multiple times")
    void shouldBeCloseableMultipleTimes() throws Exception {
        // Arrange
        BaseConfig config = BaseConfig.builder()
                .serverUrl("https://api.pispi.sn")
                .apiKey("test-api-key")
                .build();

        PiSpiClient client = PiSpiClient.getInstance(
                config,
                mockClient,
                false,
                null,
                null,
                "token"
        );

        // Act
        client.close();
        client.close(); // second close call

        // Assert
        assertThat(client.isClosed()).isTrue();
        verify(mockClient, times(2)).close();
    }

    @Test
    @DisplayName("Should use try-with-resources")
    void shouldUseTryWithResources() throws Exception {
        // Arrange
        BaseConfig config = BaseConfig.builder()
                .serverUrl("https://api.pispi.sn")
                .apiKey("test-api-key")
                .build();

        // Act
        try (PiSpiClient client = PiSpiClient.getInstance(
                config,
                mockClient,
                false,
                null,
                null,
                "token"
        )) {
            assertThat(client.isClosed()).isFalse();
        }

        // Assert
        verify(mockClient).close();
    }

    @Test
    @DisplayName("Scenario: Create client for production with mTLS")
    void scenarioCreateClientForProductionWithMtls() throws Exception {
        // Arrange
        SSLContext sslContext = SSLContext.getDefault();
        BaseConfig config = BaseConfig.builder()
                .serverUrl("https://api.pispi.sn")
                .clientId("prod-client-123")
                .clientSecret("prod-secret-456")
                .apiKey("prod-api-key-12345678")
                .build();

        // Act
        PiSpiClient client = PiSpiClient.getInstance(
                config,
                mockClient,
                false,
                sslContext,
                null,
                null
        );

        // Assert
        assertThat(client).isNotNull();
        assertThat(client.tokenManager()).isNotNull();
        assertThat(client.isClosed()).isFalse();
    }

}
