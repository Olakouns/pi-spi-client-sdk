package io.github.razacki.unit.client.filter;

import io.github.filter.ListQueryBuilder;
import io.github.provider.JacksonProvider;
import io.github.representation.CompteRepresentation;
import io.github.representation.PagedResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;


@Tag("unit")
@DisplayName("ListQueryBuilder Tests")
public class ListQueryBuilderTest {
    private MockWebServer mockServer;
    private Client client;
    private WebTarget target;

    @BeforeEach
    void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();

        client = ClientBuilder.newClient()
                .register(JacksonProvider.class);
        target = client.target(mockServer.url("/api/comptes").toString());
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

    private String loadJson(String path) {
        try (InputStream is = getClass().getResourceAsStream(path);
             Scanner scanner = new Scanner(is, "UTF-8").useDelimiter("\\A")) {

            return scanner.hasNext() ? scanner.next() : "";
        } catch (Exception e) {
            throw new RuntimeException("Cannot load JSON from " + path, e);
        }
    }

    private ListQueryBuilder<CompteRepresentation> createBuilder() {
        return new ListQueryBuilder<>(target, new GenericType<PagedResponse<CompteRepresentation>>() {});
    }

    private void mockValidResponse() {
        String jsonResponse = loadJson("/unit/mock-responses/paged-compte-response.json");
        mockServer.enqueue(new MockResponse()
                .setBody(jsonResponse)
                .setHeader("Content-Type", "application/json")
                .setResponseCode(200));
    }

    @Test
    @DisplayName("Should build query with page parameter")
    void shouldBuildQueryWithPage() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder();

        // Act
        builder.page("0").execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getPath()).contains("page=0");
    }

    @Test
    @DisplayName("Should build query with size parameter")
    void shouldBuildQueryWithSize() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder();

        // Act
        builder.size(20).execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getPath()).contains("size=20");
    }

    @Test
    @DisplayName("Should build query with page and size")
    void shouldBuildQueryWithPageAndSize() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder();

        // Act
        builder.page("0").size(20).execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getPath())
                .contains("page=0")
                .contains("size=20");
    }

    @Test
    @DisplayName("Should build query with single filter using Consumer")
    void shouldBuildQueryWithSingleFilter() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder();

        // Act
        builder
                .page("0")
                .size(20)
                .filter(f -> f.eq("numero", "00123456789"))
                .execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        assertThat(URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name()))
                .contains("page=0")
                .contains("size=20")
                .contains("numero[eq]=00123456789");
    }

    @Test
    @DisplayName("Should build query with multiple filters using Consumer")
    void shouldBuildQueryWithMultipleFilters() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder();

        // Act
        builder
                .page("0")
                .size(20)
                .filter(f -> f
                        .eq("numero", "00123456789")
                        .eq("type", "COURANT")
                        .eq("statut", "ACTIF")
                )
                .execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        assertThat(URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name()))
                .contains("numero[eq]=00123456789")
                .contains("type[eq]=COURANT")
                .contains("statut[eq]=ACTIF");
    }


    @Test
    @DisplayName("Should build query with between filter")
    void shouldBuildQueryWithBetweenFilter() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder();

        // Act
        builder
                .page("0")
                .size(20)
                .filter(f -> f.between("dateOuverture", "2024-01-01", "2024-12-31"))
                .execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        assertThat(URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name()))
                .contains("dateOuverture[gte]=2024-01-01")
                .contains("dateOuverture[lte]=2024-12-31");
    }


    @Test
    @DisplayName("Should build query with in filter")
    void shouldBuildQueryWithInFilter() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder();

        // Act
        builder
                .page("0")
                .size(20)
                .filter(f -> f.in("type", "COURANT", "EPARGNE", "DEPOT"))
                .execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        assertThat(URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name()))
                .contains("type[in]=COURANT,EPARGNE,DEPOT");
    }


    @Test
    @DisplayName("Should build query with like filter")
    void shouldBuildQueryWithLikeFilter() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder();

        // Act
        builder
                .page("0")
                .size(20)
                .filter(f -> f.like("numero", "0012%"))
                .execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        assertThat(URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name()))
                .contains("numero[like]=0012%");
    }

    @Test
    @DisplayName("Should build query with gte and lte filters")
    void shouldBuildQueryWithGteAndLteFilters() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder();

        // Act
        builder
                .page("0")
                .size(20)
                .filter(f -> f
                        .gte("solde", "1000")
                        .lte("solde", "50000")
                )
                .execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        assertThat(URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name()))
                .contains("solde[gte]=1000")
                .contains("solde[lte]=50000");
    }


    @Test
    @DisplayName("Should build query with filters from Map")
    void shouldBuildQueryWithFiltersFromMap() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder();

        Map<String, String> filters = new HashMap<>();
        filters.put("numero[eq]", "00123456789");
        filters.put("type[eq]", "COURANT");

        // Act
        builder
                .page("0")
                .size(20)
                .filters(filters)
                .execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        assertThat(URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name()))
                .contains("numero[eq]=00123456789")
                .contains("type[eq]=COURANT");
    }

    @Test
    @DisplayName("Should handle null filters Map gracefully")
    void shouldHandleNullFiltersMap() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder();

        // Act
        builder
                .page("0")
                .size(20)
                .filters(null)
                .execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        assertThat(URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name()))
                .contains("page=0")
                .contains("size=20")
                .doesNotContain("[eq]");  // Pas de filtres
    }


    @Test
    @DisplayName("Should handle empty filters Map")
    void shouldHandleEmptyFiltersMap() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder();

        // Act
        builder
                .page("0")
                .size(20)
                .filters(new HashMap<>())  // empty map
                .execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        assertThat(URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name()))
                .contains("page=0")
                .contains("size=20");
    }

    @Test
    @DisplayName("Should support method chaining")
    void shouldSupportMethodChaining() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder();

        // Act
        PagedResponse<CompteRepresentation> response = builder
                .page("0")
                .size(20)
                .filter(f -> f.eq("statut", "ACTIF"))
                .execute();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
    }

    @Test
    @DisplayName("Should allow multiple filter calls")
    void shouldAllowMultipleFilterCalls() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder();

        // Act - Plusieurs appels à filter()
        builder
                .page("0")
                .size(20)
                .filter(f -> f.eq("type", "COURANT"))
                .filter(f -> f.eq("statut", "ACTIF"))
                .filter(f -> f.gte("solde", "1000"))
                .execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        assertThat(URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name()))
                .contains("type[eq]=COURANT")
                .contains("statut[eq]=ACTIF")
                .contains("solde[gte]=1000");
    }

    @Test
    @DisplayName("Should build query without any parameters")
    void shouldBuildQueryWithoutParameters() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder();

        // Act
        builder.execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        String path = request.getPath();
        assertThat(path).doesNotContain("?");
    }

    @Test
    @DisplayName("Should handle null page parameter")
    void shouldHandleNullPageParameter() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder();

        // Act
        builder
                .page(null)
                .size(20)
                .execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        String path = request.getPath();
        assertThat(path)
                .doesNotContain("page=")
                .contains("size=20");
    }

    @Test
    @DisplayName("Should handle filter with null value")
    void shouldHandleFilterWithNullValue() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder();

        // Act
        builder
                .page("0")
                .size(20)
                .filter(f -> f.eq("numero", null))  // null value
                .execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        assertThat(URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name()))
                .contains("page=0")
                .contains("size=20")
                .doesNotContain("numero[eq]");  // Le filtre null ne doit pas être ajouté
    }

    @Test
    @DisplayName("Should handle special characters in filter values")
    void shouldHandleSpecialCharactersInFilterValues() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder();

        // Act
        builder
                .page("0")
                .size(20)
                .filter(f -> f.eq("libelle", "Compte & Épargne"))
                .execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        assertThat(URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name()))
                .contains("libelle[eq]=");
    }

    @Test
    @DisplayName("Should parse valid JSON response")
    void shouldParseValidJsonResponse() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder();

        // Act
        PagedResponse<CompteRepresentation> response = builder
                .page("1")
                .size(3)
                .execute();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData()).hasSize(3);
        assertThat(response.getMeta().getPage()).isEqualTo("1");
        assertThat(response.getMeta().getSize()).isEqualTo(3);
        assertThat(response.getMeta().getTotal()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should set correct Accept header")
    void shouldSetCorrectAcceptHeader() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder();

        // Act
        builder.page("0").size(20).execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getHeader("Accept"))
                .isEqualTo("application/json");
    }
}
