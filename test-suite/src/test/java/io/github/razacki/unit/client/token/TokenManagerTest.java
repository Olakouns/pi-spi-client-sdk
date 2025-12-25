package io.github.razacki.unit.client.token;

import io.github.BaseConfig;
import io.github.provider.ResteasyClientProvider;
import io.github.representation.AccessTokenResponse;
import io.github.token.TokenManager;
import io.github.token.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Tag("unit")
@DisplayName("TokenManager logic tests")
public class TokenManagerTest {

    private TokenManager tokenManager;
    private BaseConfig config;

    @Mock
    private TokenService mockTokenService;

    @Mock
    private ResteasyClientProvider mockProvider;

    @Captor
    private ArgumentCaptor<MultivaluedMap<String, String>> mapCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Client mockClient = mock(Client.class);
        WebTarget mockTarget = mock(WebTarget.class);

        // Configure mock behavior for target and proxy creation
        when(mockClient.target(anyString())).thenReturn(mockTarget);
        when(mockProvider.targetProxy(eq(mockTarget), eq(TokenService.class)))
                .thenReturn(mockTokenService);

        // Initialize configuration
        config = new BaseConfig();
        config.setServerUrl("https://auth.example.com");
        config.setClientId("my-client-id");
        config.setClientSecret("my-client-secret");
        config.setGrantType("client_credentials");
        config.setScopes(Collections.singletonList("api-access"));

        // Instantiate TokenManager with mocked provider
        tokenManager = new TokenManager(config, mockClient, mockProvider);
    }

    @Test
    @DisplayName("Should request a new token when currentToken is null")
    void shouldFetchTokenWhenNull() {
        // Arrange
        AccessTokenResponse response = new AccessTokenResponse();
        response.setToken("access-token-123");
        response.setExpiresIn(3600);

        // Capture the MultivaluedMap passed to grantToken
        when(mockTokenService.grantToken(mapCaptor.capture())).thenReturn(response);

        // Act
        tokenManager.getAccessToken();

        // Assert
        MultivaluedMap<String, String> sentMap = mapCaptor.getValue();
        assertEquals("client_credentials", sentMap.getFirst("grant_type"));
        assertEquals("my-client-id", sentMap.getFirst("client_id"));
        assertEquals("my-client-secret", sentMap.getFirst("client_secret"));
        assertEquals("api-access", sentMap.getFirst("scope"));
    }

    @Test
    @DisplayName("Should trigger refreshToken when access token is expired")
    void shouldRefreshTokenWhenExpired() {
        // Arrange: Prepare an expired state
        AccessTokenResponse expiredToken = new AccessTokenResponse();
        expiredToken.setToken("expired-access");
        expiredToken.setRefreshToken("valid-refresh");
        expiredToken.setExpiresIn(-60); // Expired 1 minute ago
        expiredToken.setRefreshExpiresIn(1000);

        when(mockTokenService.grantToken(any())).thenReturn(expiredToken);

        AccessTokenResponse refreshedToken = new AccessTokenResponse();
        refreshedToken.setToken("new-access");
        refreshedToken.setExpiresIn(3600);

        when(mockTokenService.refreshToken(mapCaptor.capture())).thenReturn(refreshedToken);

        // Act
        tokenManager.getAccessToken(); // First call: grant
        String result = tokenManager.getAccessTokenString(); // Second call: refresh

        // Assert
        assertEquals("new-access", result);
        MultivaluedMap<String, String> refreshMap = mapCaptor.getValue();
        assertEquals("refresh_token", refreshMap.getFirst("grant_type"));
        assertEquals("valid-refresh", refreshMap.getFirst("refresh_token"));
    }

    @Test
    @DisplayName("Should fall back to grantToken if refreshToken fails")
    void shouldFallbackToGrantOnRefreshError() {
        // Arrange
        AccessTokenResponse expiredToken = new AccessTokenResponse();
        expiredToken.setToken("expired");
        expiredToken.setRefreshToken("revoked-refresh");
        expiredToken.setExpiresIn(-10);
        expiredToken.setRefreshExpiresIn(500);

        when(mockTokenService.grantToken(any())).thenReturn(expiredToken);
        when(mockTokenService.refreshToken(any())).thenThrow(new BadRequestException());

        // Act
        tokenManager.getAccessToken(); // State initialization
        tokenManager.getAccessToken(); // Attempt refresh -> fail -> call grant again

        // Assert
        verify(mockTokenService, times(2)).grantToken(any());
    }

    @Test
    @DisplayName("Should force full re-auth after manual invalidation")
    void shouldForceReAuthAfterInvalidate() {
        // Arrange
        AccessTokenResponse token = new AccessTokenResponse();
        token.setToken("active-token");
        token.setExpiresIn(3600);
        when(mockTokenService.grantToken(any())).thenReturn(token);

        // Act
        tokenManager.getAccessToken();
        tokenManager.invalidate("active-token");
        tokenManager.getAccessToken();

        // Assert
        verify(mockTokenService, times(2)).grantToken(any());
    }

}
