package io.github.razacki.unit.client.filter;

import io.github.razacki.TestUtils;
import io.github.razacki.filter.ListQueryBuilder;
import io.github.razacki.provider.JacksonProvider;
import io.github.razacki.representation.CompteRepresentation;
import io.github.razacki.representation.PagedResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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


    private ListQueryBuilder<CompteRepresentation> createBuilder() {
        return new ListQueryBuilder<>(target, new GenericType<PagedResponse<CompteRepresentation>>() {
        });
    }

    private void mockValidResponse() {
        String jsonResponse = TestUtils.loadJson("/unit/mock-responses/paged-compte-response.json");
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

    @Test
    @DisplayName("Should add descending sort")
    void shouldAddDescendingSort() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder()
                .sortDesc("dateCreation");

        // Act
        builder.execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        assertThat(URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name()))
                .contains("sort=-dateCreation");
    }

    @Test
    @DisplayName("Should add ascending sort")
    void shouldAddAscendingSort() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder()
                .sortAsc("dateCreation");

        // Act
        builder.execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        assertThat(URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name()))
                .contains("sort=dateCreation");
    }

    @Test
    @DisplayName("Should add sort with boolean direction")
    void shouldAddSortWithBooleanDirection() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder()
                .sort("type", true);

        // Act
        builder.execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        assertThat(URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name()))
                .contains("sort=type");


        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder2 = createBuilder()
                .sort("type", false);

        builder2.execute();

        // Assert
        RecordedRequest request2 = mockServer.takeRequest();
        Assertions.assertNotNull(request2.getPath());
        assertThat(URLDecoder.decode(request2.getPath(), StandardCharsets.UTF_8.name()))
                .contains("sort=-type");

    }

    @Test
    @DisplayName("Should add multiple sorts using varargs (Object...) with directions")
    void shouldAddMultipleSortsWithVarargs() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder()
                .sort("name", true, "createdAt", false);

        // Act
        builder.execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        assertThat(URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name()))
                .contains("sort=name,-createdAt");
    }

    @Test
    @DisplayName("Should IGNORE everything if varargs length is odd")
    void shouldIgnoreEverythingWhenLengthIsOdd() throws Exception {
        // Arrange
        mockValidResponse();
        ListQueryBuilder<CompteRepresentation> builder = createBuilder()
                .sort("name", true, "type");

        // Act
        builder.execute();

        // Assert
        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        assertThat(URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name()))
                .doesNotContain("sort");
    }

    @Test
    @DisplayName("Should add multiple sort fields")
    void shouldAddMultipleSortFields() throws Exception {
        mockValidResponse();
        createBuilder()
                .sortAsc("type")
                .sortDesc("dateCreation")
                .sortAsc("numero")
                .execute();

        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        assertThat(URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name()))
                .contains("sort=type,-dateCreation,numero");
    }

    @Test
    @DisplayName("Should handle mixed sort directions with boolean sort method")
    void shouldHandleMixedSortDirections() throws Exception {
        mockValidResponse();
        createBuilder()
                .sort("dateCreation", false)
                .sort("type", true)
                .sort("solde", false)
                .execute();

        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        assertThat(URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name()))
                .contains("sort=-dateCreation,type,-solde");
    }


    @Test
    @DisplayName("Should ignore invalid sort fields (null, empty, blank)")
    void shouldIgnoreInvalidSortFields() throws Exception {
        mockValidResponse();
        createBuilder()
                .sortAsc(null)
                .sortDesc("")
                .sortDesc("   ")
                .sortDesc(null)
                .sortAsc("   ")
                .execute();

        RecordedRequest request = mockServer.takeRequest();
        assertThat(request.getPath()).doesNotContain("sort=");
    }

    @Test
    @DisplayName("Should handle varargs sort correctly")
    void shouldHandleVarargsSort() throws Exception {
        mockValidResponse();

        createBuilder().sort("type", true, "solde", false).execute();
        RecordedRequest request1 = mockServer.takeRequest();
        Assertions.assertNotNull(request1.getPath());
        assertThat(URLDecoder.decode(request1.getPath(), StandardCharsets.UTF_8.name())).contains("sort=type,-solde");


        mockValidResponse();
        createBuilder().sort("type", true, "solde").execute();
        RecordedRequest request2 = mockServer.takeRequest();
        assertThat(request2.getPath()).doesNotContain("sort=");


        mockValidResponse();
        createBuilder().sort((Object[]) null).execute();
        RecordedRequest request3 = mockServer.takeRequest();
        assertThat(request3.getPath()).doesNotContain("sort=");
    }

    @Test
    @DisplayName("Should throw ClassCastException if direction in varargs is not boolean")
    void shouldThrowExceptionWhenDirectionIsNotBoolean() {
        ListQueryBuilder<CompteRepresentation> builder = createBuilder();

        Assertions.assertThrows(ClassCastException.class, () -> {
            builder.sort("name", "not-a-boolean");
        });
    }

    @Test
    @DisplayName("Should clear")
    void shouldClearOnlySort() throws Exception {
        mockValidResponse();
        createBuilder()
                .sort("dateCreation", false)
                .sort("type", true)
                .clearSort()
                .execute();

        RecordedRequest request = mockServer.takeRequest();
        Assertions.assertNotNull(request.getPath());
        assertThat(URLDecoder.decode(request.getPath(), StandardCharsets.UTF_8.name()))
                .doesNotContain("sort");
    }


}
