package io.github.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.exception.PiSpiApiException;
import io.github.representation.ApiErrorResponse;
import org.jboss.logging.Logger;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class PiSpiClientResponseFilter implements ClientResponseFilter {

    private static final Logger logger = Logger.getLogger(PiSpiClientResponseFilter.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        int status = responseContext.getStatus();
        String method = requestContext.getMethod();
        String uri = requestContext.getUri().toString();

        if (logger.isDebugEnabled()) {
            logResponse(method, uri, status, responseContext);
        }

        if (status >= 400 && status != 401) {
            handleErrorResponse(method, uri, status, responseContext);
        }
    }


    private void logResponse(String method, String uri, int status, ClientResponseContext responseContext) {
        logger.debugf("Response: %s %s - Status: %d", method, uri, status);

        if (logger.isTraceEnabled()) {
            responseContext.getHeaders().forEach((key, values) ->
                    logger.tracef("  Header: %s = %s", key, values)
            );
        }
    }

    private void handleErrorResponse(String method, String uri, int status, ClientResponseContext responseContext) throws IOException {
        String errorBody = readResponseBody(responseContext);
        ApiErrorResponse errorResponse = parseErrorResponse(errorBody, status);
        logError(method, uri, status, errorResponse, errorBody);
        throw createException(status, errorResponse);
    }

    private String readResponseBody(ClientResponseContext responseContext) throws IOException {
        if (!responseContext.hasEntity()) {
            return "";
        }

        InputStream entityStream = responseContext.getEntityStream();
        if (entityStream == null) {
            return "";
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(entityStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    private ApiErrorResponse parseErrorResponse(String errorBody, int status) {
        if (errorBody == null || errorBody.trim().isEmpty()) {
            return createFallbackError(status);
        }

        try {
            return objectMapper.readValue(errorBody, ApiErrorResponse.class);
        } catch (Exception e) {
            logger.debugf("Could not parse error response as RFC 7807 format: %s", e.getMessage());
            ApiErrorResponse fallback = new ApiErrorResponse();
            fallback.setTitle("HTTP Error " + status);
            fallback.setDetail(errorBody);
            return fallback;
        }
    }

    private void logError(String method, String uri, int status, ApiErrorResponse errorResponse, String errorBody) {
        String message = String.format("API Error: %s %s - Status: %d", method, uri, status);

        if (errorResponse != null) {
            StringBuilder logMsg = new StringBuilder(message);

            if (errorResponse.getTitle() != null) {
                logMsg.append(" - Title: ").append(errorResponse.getTitle());
            }
            if (errorResponse.getDetail() != null) {
                logMsg.append(", Detail: ").append(errorResponse.getDetail());
            }
            if (errorResponse.getInstance() != null) {
                logMsg.append(", Instance: ").append(errorResponse.getInstance());
            }

            if (errorResponse.getInvalidParams() != null && !errorResponse.getInvalidParams().isEmpty()) {
                logMsg.append(", Invalid params: [");
                errorResponse.getInvalidParams().forEach(param ->
                        logMsg.append(param.getName()).append("='").append(param.getReason()).append("', ")
                );
                logMsg.append("]");
            }

            logger.errorf(logMsg.toString());
        } else if (errorBody != null && !errorBody.isEmpty()) {
            logger.errorf("API Error [%s %s] Status: %d - Body: %s", method, uri, status, errorBody);
        } else {
            logger.errorf("API Error [%s %s] Status: %d - No error details available", method, uri, status);
        }
    }

    private PiSpiApiException createException(int status, ApiErrorResponse errorResponse) {
        String message = extractErrorMessage(errorResponse, status);
        return new PiSpiApiException(message, errorResponse);
    }


    private String extractErrorMessage(ApiErrorResponse errorResponse, int status) {
        if (errorResponse != null) {
            if (errorResponse.getTitle() != null && !errorResponse.getTitle().isEmpty()) {
                return errorResponse.getTitle();
            }
            if (errorResponse.getDetail() != null && !errorResponse.getDetail().isEmpty()) {
                return errorResponse.getDetail();
            }
        }
        return "HTTP " + status + " error";
    }

    private ApiErrorResponse createFallbackError(int status) {
        ApiErrorResponse fallback = new ApiErrorResponse();
        fallback.setStatus(status);
        fallback.setTitle("HTTP Error " + status);
        fallback.setDetail("The server returned an error without a detailed message.");
        return fallback;
    }
}
