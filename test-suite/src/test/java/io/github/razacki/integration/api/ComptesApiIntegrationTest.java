package io.github.razacki.integration.api;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import io.github.exception.PiSpiApiException;
import io.github.filter.ApiKeyFilter;
import io.github.razacki.integration.AbstractIntegrationTest;
import io.github.representation.CompteRepresentation;
import io.github.representation.PagedResponse;
import io.github.representation.enums.CompteStatut;
import io.github.representation.enums.CompteType;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.*;

import javax.ws.rs.core.MediaType;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Tag("integration")
@DisplayName("Comptes API Integration Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ComptesApiIntegrationTest extends AbstractIntegrationTest {

    @Test
    @Order(1)
    @DisplayName("Should list comptes with pagination")
    void shouldListComptesWithPagination() {
        // Arrange
        stubFor(get(urlPathEqualTo("/comptes"))
                .withQueryParam("page", equalTo("0"))
                .withQueryParam("size", equalTo("20"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBodyFile("comptes-list-success.json")));

        // Act
        PagedResponse<CompteRepresentation> response = client.api().comptes()
                .list(0, 20);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotEmpty();
        assertThat(response.getData()).hasSize(3);
        assertThat(response.getMeta().getPage()).isEqualTo("0");
        assertThat(response.getMeta().getSize()).isEqualTo(20);

        // Verify the request
        verify(1, getRequestedFor(urlPathEqualTo("/comptes"))
                .withQueryParam("page", equalTo("0"))
                .withQueryParam("size", equalTo("20"))
                .withHeader(ApiKeyFilter.API_KEY_HEADER, containing(API_KEY))
                .withHeader(HttpHeaders.AUTHORIZATION, containing(AUTH_TOKEN)));
    }

    @Test
    @Order(2)
    @DisplayName("Should filter comptes by type and status")
    void shouldFilterComptesByTypeAndStatus() {
        // Arrange
        stubFor(get(urlPathMatching("/comptes.*"))
                .withQueryParam("type[eq]", equalTo(CompteType.CACC.name()))
                .withQueryParam("statut[eq]", equalTo(CompteStatut.OUVERT.name()))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBodyFile("comptes-filtered-success.json")));

        // Act
        PagedResponse<CompteRepresentation> response = client.api().comptes()
                .query()
                .page("0")
                .size(20)
                .filter(f -> f
                        .eq("type", CompteType.CACC)
                        .eq("statut", CompteStatut.OUVERT)
                )
                .execute();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotEmpty();
        assertThat(response.getData())
                .allMatch(c -> CompteType.CACC.equals(c.getType()))
                .allMatch(c -> CompteStatut.OUVERT.equals(c.getStatut()));

        // Verify the request and query parameters
        verify(getRequestedFor(urlPathEqualTo("/comptes"))
                .withQueryParam("type[eq]", equalTo("CACC"))
                .withQueryParam("statut[eq]", equalTo("OUVERT")));
    }

    @Test
    @Order(3)
    @DisplayName("Should filter comptes by date range")
    void shouldFilterComptesByDateRange() {
        // Arrange
        stubFor(get(urlPathMatching("/comptes.*"))
                .withQueryParam("dateOuverture[gte]", equalTo("2024-01-01"))
                .withQueryParam("dateOuverture[lte]", equalTo("2024-12-31"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBodyFile("comptes-list-success.json")));

        // Act
        PagedResponse<CompteRepresentation> response = client.api().comptes()
                .query()
                .page("0")
                .size(50)
                .filter(f -> f.between("dateOuverture", "2024-01-01", "2024-12-31"))
                .execute();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotEmpty();

        verify(getRequestedFor(urlPathEqualTo("/comptes"))
                .withQueryParam("dateOuverture[gte]", equalTo("2024-01-01"))
                .withQueryParam("dateOuverture[lte]", equalTo("2024-12-31")));
    }

    @Test
    @Order(4)
    @DisplayName("Should sort comptes by dateCreation descending")
    void shouldSortComptesByDateCreationDesc() {
        // Arrange
        stubFor(get(urlPathMatching("/comptes.*"))
                .withQueryParam("sort", equalTo("-dateCreation"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBodyFile("comptes-list-success.json")));

        // Act
        PagedResponse<CompteRepresentation> response = client.api().comptes()
                .query()
                .page("0")
                .size(20)
                .filter(f -> f.sortDesc("dateCreation"))
                .execute();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getData()).isNotEmpty();

        verify(getRequestedFor(urlPathEqualTo("/comptes"))
                .withQueryParam("sort", equalTo("-dateCreation")));
    }

    @Test
    @Order(5)
    @DisplayName("Should handle complex query with multiple filters and sort")
    void shouldHandleComplexQuery() {
        // Arrange
        stubFor(get(urlPathMatching("/comptes.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBodyFile("comptes-list-success.json")));

        // Act
        PagedResponse<CompteRepresentation> response = client.api().comptes()
                .query()
                .page("0")
                .size(30)
                .filter(f -> f
                        .in("type", "CACC", "CARD")
                        .gte("numero", "CIC2344256727788288822")
                        .sortAsc("type")
                        .sortDesc("numero")
                        .where("statut", "OUVERT")
                )
                .execute();

        // Assert
        assertThat(response).isNotNull();

        verify(getRequestedFor(urlPathEqualTo("/comptes"))
                .withQueryParam("type[in]", equalTo("CACC,CARD"))
                .withQueryParam("statut", equalTo("OUVERT"))
                .withQueryParam("numero[gte]", equalTo("CIC2344256727788288822"))
                .withQueryParam("sort", equalTo("type,-numero")));
    }

    @Test
    @Order(6)
    @DisplayName("Should handle pagination across multiple pages")
    void shouldHandlePaginationAcrossMultiplePages() {
        // Arrange - Page 1
        stubFor(get(urlPathEqualTo("/comptes"))
                .withQueryParam("page", equalTo("0"))
                .withQueryParam("size", equalTo("10"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBodyFile("comptes-page-1.json")));

        // Arrange - Page 2
        stubFor(get(urlPathEqualTo("/comptes"))
                .withQueryParam("page", equalTo("1"))
                .withQueryParam("size", equalTo("10"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBodyFile("comptes-page-2.json")));

        // Act
        PagedResponse<CompteRepresentation> page1 = client.api().comptes().list(0, 10);
        PagedResponse<CompteRepresentation> page2 = client.api().comptes().list(1, 10);

        // Assert
        assertThat(page1.getData()).hasSize(10);
        assertThat(page2.getData()).hasSize(10);
        assertThat(page1.getMeta().getPage()).isEqualTo("0");
        assertThat(page2.getMeta().getPage()).isEqualTo("1");

        verify(2, getRequestedFor(urlPathEqualTo("/comptes")));
    }

    @Test
    @Order(7)
    @DisplayName("Should handle empty result set")
    void shouldHandleEmptyResultSet() {
        // Arrange
        stubFor(get(urlPathMatching("/comptes.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBody("{\n" +
                                "  \"data\": [],\n" +
                                "  \"meta\": {\n" +
                                "    \"page\": 0,\n" +
                                "    \"size\": 20,\n" +
                                "    \"total\": 0\n" +
                                "  }\n" +
                                "}")));

        // Act
        PagedResponse<CompteRepresentation> response = client.api().comptes()
                .query()
                .page("0")
                .size(20)
                .filter(f -> f.eq("numero", "99999999"))
                .execute();

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getData()).isEmpty();
        assertThat(response.getMeta().getTotal()).isEqualTo(0);
    }

    @Test
    @Order(8)
    @DisplayName("Should throw AuthenticationException on 401")
    void shouldThrowAuthenticationExceptionOn401() {
        // Arrange
        stubFor(get(urlPathEqualTo("/comptes"))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBodyFile("unauthorized.json")));

        // Act & Assert
        assertThatThrownBy(() -> client.api().comptes().list(0, 20))
                .isInstanceOf(RuntimeException.class)
                .hasRootCauseInstanceOf(PiSpiApiException.class)
                .hasMessageContaining("Unauthorized");
    }

    @Test
    @Order(9)
    @DisplayName("Should throw AuthorizationException on 403")
    void shouldThrowAuthorizationExceptionOn403() {
        // Arrange
        stubFor(get(urlPathEqualTo("/comptes"))
                .willReturn(aResponse()
                        .withStatus(403)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBodyFile("forbidden.json")));

        // Act & Assert
        assertThatThrownBy(() -> client.api().comptes().list(0, 20))
                .hasRootCauseInstanceOf(PiSpiApiException.class)
                .cause()
                .extracting("errorResponse")
                .extracting("detail")
                .asString()
                .contains("Le solde du compte est insuffisant pour effectuer ce transfert.");
    }

    @Test
    @Order(10)
    @DisplayName("Should throw ValidationException on 400 with invalid params")
    void shouldThrowValidationExceptionOn400() {
        // Arrange bad-request.json
        stubFor(get(urlPathMatching("/comptes.*"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBodyFile("bad-request.json")));

        // Act & Assert
        assertThatThrownBy(() -> client.api().comptes()
                .query()
                .page("-1")
                .size(20)
                .execute())
                .isInstanceOf(RuntimeException.class)
                .hasRootCauseInstanceOf(PiSpiApiException.class)
                .hasMessageContaining("Bad Request");
    }

    @Test
    @Order(11)
    @DisplayName("Should throw RateLimitException on 429")
    void shouldThrowRateLimitExceptionOn429() {
        // Arrange
        stubFor(get(urlPathEqualTo("/comptes"))
                .willReturn(aResponse()
                        .withStatus(429)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withHeader("Retry-After", "60")
                        .withBodyFile("rate-limit-exception.json")));

        // Act & Assert
        assertThatThrownBy(() -> client.api().comptes().list(0, 20))
                .isInstanceOf(RuntimeException.class)
                .hasRootCauseInstanceOf(PiSpiApiException.class)
                .hasMessageContaining("Too Many Requests");
    }

    @Test
    @Order(12)
    @DisplayName("Should throw ServerException on 500")
    void shouldThrowServerExceptionOn500() {
        // Arrange
        stubFor(get(urlPathEqualTo("/comptes"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBodyFile("internal-server-error.json")));

        // Act & Assert
        assertThatThrownBy(() -> client.api().comptes().list(0, 20))
                .isInstanceOf(RuntimeException.class)
                .hasRootCauseInstanceOf(PiSpiApiException.class)
                .hasMessageContaining("Internal Server Error");
    }

    @Test
    @Order(13)
    @DisplayName("Should throw ServerException on 503 Service Unavailable")
    void shouldThrowServerExceptionOn503() {
        // Arrange
        stubFor(get(urlPathEqualTo("/comptes"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBodyFile("service-unavailable.json")));

        // Act & Assert
        assertThatThrownBy(() -> client.api().comptes().list(0, 20))
                .isInstanceOf(RuntimeException.class)
                .hasRootCauseInstanceOf(PiSpiApiException.class)
                .hasMessageContaining("Service Unavailable");
    }

    @Test
    @Order(14)
    @DisplayName("Should handle API latency gracefully")
    void shouldHandleApiLatencyGracefully() {
        // Arrange with 2-second delay
        stubFor(get(urlPathEqualTo("/comptes"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBodyFile("comptes-list-success.json")
                        .withFixedDelay(2000)));

        // Act
        long startTime = System.currentTimeMillis();
        PagedResponse<CompteRepresentation> response = client.api().comptes().list(0, 20);
        long duration = System.currentTimeMillis() - startTime;

        // Assert
        assertThat(response).isNotNull();
        assertThat(duration).isGreaterThanOrEqualTo(2000);
    }

    @Test
    @Order(15)
    @DisplayName("Should handle network errors gracefully")
    void shouldHandleNetworkErrorsGracefully() {
        // Arrange - Simulate network error
        stubFor(get(urlPathEqualTo("/comptes"))
                .willReturn(aResponse()
                        .withFault(com.github.tomakehurst.wiremock.http.Fault.CONNECTION_RESET_BY_PEER)));

        // Act & Assert
        assertThatThrownBy(() -> client.api().comptes().list(0, 20))
                .isInstanceOf(Exception.class);
    }

    @AfterEach
    void verifyNoUnmatchedRequests() {
        // Verify that there are no unmatched requests
        List<LoggedRequest> unmatched =
                findAll(anyRequestedFor(anyUrl()));

        // Log unmatched requests if any
        if (!unmatched.isEmpty()) {
            System.out.println("Requests made: " + unmatched.size());
        }
        WireMock.reset();
    }
}
