package io.github.razacki.unit.client.filter;

import io.github.filter.BearerAuthFilter;
import io.github.token.TokenManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Tag("unit")
@DisplayName("Bearer Authentication Filter Tests")
public class BearerAuthFilterTest {

    private TokenManager mockTokenManager;
    private ClientRequestContext mockRequestContext;
    private ClientResponseContext mockResponseContext;
    private MultivaluedMap<String, Object> headers;

    @BeforeEach
    void setUp() {
        mockTokenManager = mock(TokenManager.class);
        mockRequestContext = mock(ClientRequestContext.class);
        mockResponseContext = mock(ClientResponseContext.class);
        headers = new MultivaluedHashMap<>();

        // Ensure getHeaders() returns our local map for inspection
        when(mockRequestContext.getHeaders()).thenReturn(headers);
    }

    @Test
    @DisplayName("Should add Bearer prefix if the token string is missing it")
    void shouldAddBearerPrefixToRawToken() throws Exception {
        // --- ARRANGE ---
        String rawToken = "secret-123";
        BearerAuthFilter filter = new BearerAuthFilter(rawToken);

        // --- ACT ---
        filter.filter(mockRequestContext);

        // --- ASSERT ---
        assertEquals("Bearer secret-123", headers.getFirst(HttpHeaders.AUTHORIZATION),
                "Filter should have prepended 'Bearer ' to the raw token");
    }

    @Test
    @DisplayName("Should fetch current token from TokenManager during request")
    void shouldFetchTokenFromManager() throws Exception {
        // --- ARRANGE ---
        String dynamicToken = "dynamic-token-456";
        when(mockTokenManager.getAccessTokenString()).thenReturn(dynamicToken);
        BearerAuthFilter filter = new BearerAuthFilter(mockTokenManager);

        // --- ACT ---
        filter.filter(mockRequestContext);

        // --- ASSERT ---
        assertEquals("Bearer dynamic-token-456", headers.getFirst(HttpHeaders.AUTHORIZATION));
        verify(mockTokenManager, times(1)).getAccessTokenString();
    }

    @Test
    @DisplayName("Should invalidate token when server returns 401 Unauthorized")
    void shouldInvalidateTokenOn401Response() throws Exception {
        // --- ARRANGE ---
        String expiredToken = "expired-token-789";
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + expiredToken);

        when(mockResponseContext.getStatus()).thenReturn(401);
        BearerAuthFilter filter = new BearerAuthFilter(mockTokenManager);

        // --- ACT ---
        filter.filter(mockRequestContext, mockResponseContext);

        // --- ASSERT ---
        verify(mockTokenManager, times(1)).invalidate(expiredToken);
    }

    @Test
    @DisplayName("Should not invalidate token when response status is successful (200 OK)")
    void shouldNotInvalidateOnSuccess() throws Exception {
        // --- ARRANGE ---
        when(mockResponseContext.getStatus()).thenReturn(200);
        BearerAuthFilter filter = new BearerAuthFilter(mockTokenManager);

        // --- ACT ---
        filter.filter(mockRequestContext, mockResponseContext);

        // --- ASSERT ---
        verify(mockTokenManager, never()).invalidate(anyString());
    }
}
