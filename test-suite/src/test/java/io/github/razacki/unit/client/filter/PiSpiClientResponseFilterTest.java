package io.github.razacki.unit.client.filter;

import io.github.exception.PiSpiApiException;
import io.github.filter.PiSpiClientResponseFilter;
import io.github.representation.ApiErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
@DisplayName("PiSpiClientResponseFilter Unit Tests")
public class PiSpiClientResponseFilterTest {

    private PiSpiClientResponseFilter filter;
    private ClientRequestContext mockRequestContext;
    private ClientResponseContext mockResponseContext;

    @BeforeEach
    void setUp() {
        filter = new PiSpiClientResponseFilter();
        mockRequestContext = mock(ClientRequestContext.class);
        mockResponseContext = mock(ClientResponseContext.class);


        when(mockRequestContext.getMethod()).thenReturn("GET");
        when(mockRequestContext.getUri()).thenReturn(URI.create("https://api.pi-spi.io/v1/test"));
    }

    @Test
    @DisplayName("Should parse complex JSON error with invalid-params list")
    void shouldParseComplexJsonWithInvalidParams() {
        // --- ARRANGE ---
        int status = 403;
        String jsonError = "{\n" +
                "  \"type\": \"about:blank\",\n" +
                "  \"title\": \"Forbidden\",\n" +
                "  \"status\": 403,\n" +
                "  \"detail\": \"Le solde du compte est insuffisant pour effectuer ce transfert.\",\n" +
                "  \"instance\": \"/comptes/transactions/23511722\",\n" +
                "  \"invalid-params\": [\n" +
                "    {\n" +
                "      \"name\": \"montant\",\n" +
                "      \"reason\": \"Le montant est supérieur au solde disponible.\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        InputStream entityStream = new ByteArrayInputStream(jsonError.getBytes(StandardCharsets.UTF_8));

        when(mockResponseContext.getStatus()).thenReturn(status);
        when(mockResponseContext.hasEntity()).thenReturn(true);
        when(mockResponseContext.getEntityStream()).thenReturn(entityStream);

        // --- ACT ---
        PiSpiApiException exception = assertThrows(PiSpiApiException.class, () ->
                filter.filter(mockRequestContext, mockResponseContext)
        );

        // --- ASSERT ---
        ApiErrorResponse error = exception.getErrorResponse();
        assertNotNull(error, "Error response should not be null");
        assertEquals("Forbidden", error.getTitle());
        assertEquals(403, error.getStatus());

        // Verifying the invalidParams list
        assertNotNull(error.getInvalidParams(), "Invalid params list should be present");
        assertEquals(1, error.getInvalidParams().size());

        ApiErrorResponse.InvalidParam param = error.getInvalidParams().get(0);
        assertEquals("montant", param.getName());
        assertEquals("Le montant est supérieur au solde disponible.", param.getReason());
    }

    @Test
    @DisplayName("Should use fallback error when body is empty")
    void shouldHandleEmptyErrorBody() {
        // --- ARRANGE ---
        when(mockResponseContext.getStatus()).thenReturn(500);
        when(mockResponseContext.hasEntity()).thenReturn(false);

        // --- ACT & ASSERT ---
        PiSpiApiException exception = assertThrows(PiSpiApiException.class, () ->
                filter.filter(mockRequestContext, mockResponseContext)
        );

        assertEquals("HTTP Error 500", exception.getErrorResponse().getTitle());
        assertEquals(500, exception.getErrorResponse().getStatus());
    }

    @Test
    @DisplayName("Should handle malformed JSON by creating a fallback with raw body")
    void shouldHandleMalformedJson() throws IOException {
        // --- ARRANGE ---
        String rawBody = "Internal Server Error - Plain Text";
        InputStream entityStream = new ByteArrayInputStream(rawBody.getBytes(StandardCharsets.UTF_8));

        when(mockResponseContext.getStatus()).thenReturn(500);
        when(mockResponseContext.hasEntity()).thenReturn(true);
        when(mockResponseContext.getEntityStream()).thenReturn(entityStream);

        // --- ACT & ASSERT ---
        PiSpiApiException exception = assertThrows(PiSpiApiException.class, () ->
                filter.filter(mockRequestContext, mockResponseContext)
        );

        assertEquals("HTTP Error 500", exception.getErrorResponse().getTitle());
        assertEquals(rawBody, exception.getErrorResponse().getDetail());
    }

    @Test
    @DisplayName("Should do nothing when status is 200 OK")
    void shouldIgnoreSuccessStatus() throws IOException {
        // --- ARRANGE ---
        when(mockResponseContext.getStatus()).thenReturn(200);

        // --- ACT & ASSERT ---
        assertDoesNotThrow(() -> filter.filter(mockRequestContext, mockResponseContext));
        verify(mockResponseContext, never()).getEntityStream();
    }

    @Test
    @DisplayName("Should ignore 401 status (managed by BearerAuthFilter)")
    void shouldIgnore401Status() throws IOException {
        // ARRANGE ---
        when(mockResponseContext.getStatus()).thenReturn(401);

        // ACT & ASSERT ---
        PiSpiApiException exception = assertThrows(PiSpiApiException.class, () ->
                filter.filter(mockRequestContext, mockResponseContext)
        );

        assertEquals("HTTP Error 401", exception.getErrorResponse().getTitle());
        assertEquals(401, exception.getErrorResponse().getStatus());
    }
}
