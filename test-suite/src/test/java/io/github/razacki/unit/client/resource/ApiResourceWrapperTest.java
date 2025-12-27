package io.github.razacki.unit.client.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.PiSpiClient;
import io.github.provider.JacksonProvider;
import io.github.representation.EnrollmentRepresentation;
import io.github.resource.ApiResource;
import io.github.resource.ComptesResource;
import io.github.resource.wrapper.*;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.http.HttpHeaders;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ApiResourceWrapperTest {

    private static MockWebServer mockServer;
    private static ObjectMapper objectMapper;
    private ApiResourceWrapper wrapper;
    private Client client;

    @AfterEach
    void tearDown() throws IOException {
        if (client != null) {
            client.close();
        }
        if (mockServer != null) {
            mockServer.shutdown();
        }
    }

    @BeforeEach
    void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();

        String baseUrl = mockServer.url("/").toString();
        client = ClientBuilder.newClient().register(JacksonProvider.class);
        WebTarget rootTarget = client.target(baseUrl);

        ApiResource proxy = ((ResteasyWebTarget) rootTarget).proxy(ApiResource.class);

        wrapper = new ApiResourceWrapper(proxy, rootTarget.path("/"));
    }

    @Test
    @DisplayName("Should initialize and cache sub-wrappers (Singleton pattern)")
    void shouldCacheSubWrappers() {
        // Act: Call methods multiple times
        ComptesResourceWrapper c1 = wrapper.comptes();
        ComptesResourceWrapper c2 = wrapper.comptes();

        WebhookResourceWrapper w1 = wrapper.webhooks();
        WebhookResourceWrapper w2 = wrapper.webhooks();

        DemandesPaiementsResourceWrapper d1 = wrapper.demandesPaiements();
        DemandesPaiementsResourceWrapper d2 = wrapper.demandesPaiements();

        DemandePaiementGroupeResourceWrapper dG1 = wrapper.demandesPaiementsGroupes();
        DemandePaiementGroupeResourceWrapper dG2 = wrapper.demandesPaiementsGroupes();

        // Assert: Verify they are not null and instances are reused
        assertThat(c1).isNotNull().isSameAs(c2);
        assertThat(w1).isNotNull().isSameAs(w2);
        assertThat(d1).isNotNull().isSameAs(d2);
        assertThat(dG1).isNotNull().isSameAs(dG2);
    }

    @Test
    @DisplayName("Should call correct URL for accounts")
    void testComptesPath() throws Exception {
        mockServer.enqueue(new MockResponse().setBody("{\"data\":[]}")
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

        // Act: Call sub-resource list
        wrapper.comptes().list(0, 10);

        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getPath()).contains("/comptes");
        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
    }

    @Test
    @DisplayName("Should call correct URL for webhooks")
    void testWebhooksPath() throws Exception {
        mockServer.enqueue(new MockResponse().setBody("{\"data\":[]}")
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

        // Act
        wrapper.webhooks().list(0, 5);

        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getPath()).contains("/webhooks");
    }

    @Test
    @DisplayName("Should call correct URL for payment requests")
    void testDemandesPaiementsPath() throws Exception {
        mockServer.enqueue(new MockResponse().setBody("{\"data\":[]}").addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));

        // Act
        wrapper.demandesPaiements().list(0, 1);

        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getPath()).contains("/demandes-paiements");
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
        EnrollmentRepresentation result = wrapper.enrollment(cle);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getAlias()).isNotNull();
        assertThat(result.getAlias().getCle()).isEqualTo(cle);

        RecordedRequest request = mockServer.takeRequest();
        // Assuming enrollment path is defined in proxy as /enrollment/{cle}/check or similar
        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET);
    }

}
