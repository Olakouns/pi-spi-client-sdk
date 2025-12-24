package io.github.razacki.unit.client.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.PiSpiClient;
import io.github.representation.CompteRepresentation;
import io.github.resource.ComptesResource;
import io.github.resource.wrapper.ComptesResourceWrapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.*;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Tag("unit")
@DisplayName("ComptesResourceWrapperTest Tests")
public class ComptesResourceWrapperTest {

    private static MockWebServer mockBackEnd;
    private WebTarget target;
    private static ObjectMapper objectMapper;
    private ComptesResourceWrapper wrapper;

    @BeforeAll
    static void setup() throws IOException {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @AfterAll
    static void tearDown() throws Exception {
        mockBackEnd.shutdown();
    }

    @BeforeEach
    void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();

        String baseUrl = mockBackEnd.url("/").toString();
        Client client = ClientBuilder.newClient();
        WebTarget rootTarget = client.target(baseUrl);

        ComptesResource proxy = PiSpiClient.getClientProvider()
                .targetProxy(rootTarget, ComptesResource.class);

        wrapper = new ComptesResourceWrapper(proxy, rootTarget.path("/comptes"));
    }

    @Test
    @DisplayName("Should call findByNumero via Proxy")
    void testFindByNumero() throws Exception {
        // Arrange
        CompteRepresentation mockCompte = new CompteRepresentation();
        mockCompte.setNumero("ACC-123");

        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockCompte))
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

        // Act
        CompteRepresentation result = wrapper.findByNumero("ACC-123");

        // Assert
        assertThat(result.getNumero()).isEqualTo("ACC-123");

        // VERIFY:
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo(HttpMethod.GET);
        assertThat(recordedRequest.getPath()).isEqualTo("/ACC-123");
    }
}
