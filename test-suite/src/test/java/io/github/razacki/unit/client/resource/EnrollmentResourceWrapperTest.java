package io.github.razacki.unit.client.resource;

import io.github.razacki.exception.PiSpiException;
import io.github.razacki.provider.JacksonProvider;
import io.github.razacki.representation.EnrollmentRepresentation;
import io.github.razacki.resource.AliasResource;
import io.github.razacki.resource.ComptesResource;
import io.github.razacki.resource.EnrollmentResource;
import io.github.razacki.resource.wrapper.AliasResourceWrapper;
import io.github.razacki.resource.wrapper.ComptesResourceWrapper;
import io.github.razacki.resource.wrapper.EnrollmentResourceWrapper;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Tag("unit")
@DisplayName("EnrollmentResourceWrapper Tests")
public class EnrollmentResourceWrapperTest {

    private MockWebServer mockServer;
    private Client client;
    private EnrollmentResourceWrapper wrapper;

    @BeforeEach
    void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();

        String baseUrl = mockServer.url("/").toString();
        client = ClientBuilder.newClient().register(JacksonProvider.class);
        WebTarget rootTarget = client.target(baseUrl);

        EnrollmentResource proxy = ((ResteasyWebTarget) rootTarget).proxy(EnrollmentResource.class);

        wrapper = new EnrollmentResourceWrapper(proxy, rootTarget.path("/alias"));
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
    @DisplayName("Should handle enrollment check call")
    void testEnrollment() throws Exception {
        // Arrange
        String cle = "550e8400-e29b-41d4-a716-446655440000";
        String mockResponse = "{\n" +
                "  \"client\": {\n" +
                "    \"nom\": \"Jean Dupont\",\n" +
                "    \"pays\": \"CI\",\n" +
                "    \"categorie\": \"P\"\n" +
                "  },\n" +
                "  \"alias\": {\n" +
                "    \"cle\": \"550e8400-e29b-41d4-a716-446655440000\"\n" +
                "  }\n" +
                "}";
        mockServer.enqueue(new MockResponse()
                .setBody(mockResponse)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

        // Act
        EnrollmentRepresentation result = wrapper.check(cle);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getAlias()).isNotNull();
        assertThat(result.getAlias().getCle()).isEqualTo(cle);

        RecordedRequest request = mockServer.takeRequest();
        // Assuming enrollment path is defined in proxy as /enrollment/{cle}/check or similar
        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
    }

    @Test
    @DisplayName("Should validate key before checking enrollment")
    void shouldThrowExceptionWhenCheckingWithInvalidKey() {
        assertThatThrownBy(() -> wrapper.check(null))
                .isInstanceOf(PiSpiException.class)
                .hasMessageContaining("Alias cle cannot be null or empty");
    }

}
