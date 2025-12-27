package io.github.razacki.unit;

import io.github.BaseConfig;
import io.github.PiSpiClient;
import io.github.provider.ResteasyClientProvider;
import io.github.resource.ApiResource;
import io.github.token.TokenService;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Tag("unit")
@DisplayName("PiSpiClient Tests")
public class PiSpiClientTest {
//    @Mock
//    private Client mockClient;
//
//    @Mock(extraInterfaces = {ResteasyWebTarget.class})
//    private WebTarget mockTarget;
//
//    @Mock
//    private ResteasyClientProvider mockProvider;
//
//    @Mock
//    private ApiResource mockApiResource;
//
//    @Mock
//    private TokenService mockTokenService;
//
//    private AutoCloseable mocks;
//
//    @BeforeEach
//    void setUp() {
//
//        mockTarget = mock(WebTarget.class, withSettings()
//                .extraInterfaces(ResteasyWebTarget.class)
//                .defaultAnswer(RETURNS_SELF)); // Important pour le chaînage
//
//        mocks = MockitoAnnotations.openMocks(this);
//
////        mockTarget = mock(WebTarget.class, withSettings()
////                .extraInterfaces(ResteasyWebTarget.class));
//
//        PiSpiClient.setClientProvider(mockProvider);
//
//        when(mockClient.target(anyString())).thenReturn(mockTarget);
//
//        when(mockTarget.register(any())).thenReturn(mockTarget);
//        when(mockTarget.register(any(), anyInt())).thenReturn(mockTarget);
//
//        when(mockProvider.targetProxy(any(WebTarget.class), eq(ApiResource.class)))
//                .thenReturn(mockApiResource);
//        when(mockProvider.targetProxy(any(WebTarget.class), eq(TokenService.class)))
//                .thenReturn(mockTokenService);
//    }
//
//    @AfterEach
//    void tearDown() throws Exception {
//        if (mocks != null) {
//            mocks.close();
//        }
//    }
//
//
//    @Test
//    @DisplayName("Should create client with basic config")
//    void shouldCreateClientWithBasicConfig() {
//        // Arrange
//        BaseConfig config = BaseConfig.builder()
//                .serverUrl("https://api.pispi.sn")
//                .apiKey("test-api-key")
//                .build();
//
//        // Act
//        PiSpiClient client = PiSpiClient.getInstance(
//                config,
//                mockClient,
//                false,
//                null,
//                null,
//                null
//        );
//
//        // Assert
//        assertThat(client).isNotNull();
//        assertThat(client.isClosed()).isFalse();
//
//        // Vérifier les interactions
//        verify(mockClient, times(2)).target("https://api.pispi.sn"); // 1 pour le client + 1 pour TokenManager
//        verify(mockTarget, atLeast(2)).register(any()); // AuthFilter + ResponseFilter + ApiKeyFilter
//    }
}
