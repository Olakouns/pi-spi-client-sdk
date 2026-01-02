package io.github.olakouns.unit.client.filter;

import io.github.olakouns.filter.ApiKeyFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("unit")
@DisplayName("ApiKeyFilter Tests")
public class ApiKeyFilterTest {

    private ClientRequestContext mockContext;
    private MultivaluedMap<String, Object> headers;

    @BeforeEach
    void setUp() {
        // Initialisation commune des mocks
        mockContext = mock(ClientRequestContext.class);
        headers = new MultivaluedHashMap<>();
        when(mockContext.getHeaders()).thenReturn(headers);
    }

    @Test
    @DisplayName("Should add X-API-Key header when API key is provided")
    void shouldAddApiKeyHeaderWhenApiKeyIsPresent() throws Exception {
        // --- ARRANGE ---
        String testKey = "pi-spi-12345";
        ApiKeyFilter filter = new ApiKeyFilter(testKey);

        // ACT
        filter.filter(mockContext);

        // ASSERT
        assertEquals(testKey, headers.getFirst(ApiKeyFilter.API_KEY_HEADER),
                "The X-API-Key header should match the provided API key");
    }

    @Test
    @DisplayName("Should not add any header when API key is null")
    void shouldNotAddHeaderWhenApiKeyIsNull() throws Exception {
        // ARRANGE
        ApiKeyFilter filter = new ApiKeyFilter(null);

        // ACT
        filter.filter(mockContext);

        // ASSERT
        assertNull(headers.getFirst(ApiKeyFilter.API_KEY_HEADER),
                "No API key header should be present");
        assertEquals(0, headers.size(),
                "The headers map should remain empty");
    }
}
