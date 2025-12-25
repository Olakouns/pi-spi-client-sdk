package io.github.exception;

import io.github.representation.ApiErrorResponse;

public class PiSpiApiException extends RuntimeException {
    private final ApiErrorResponse errorResponse;

    public PiSpiApiException(String message, ApiErrorResponse errorResponse) {
        super(message);
        this.errorResponse = errorResponse;
    }

    public ApiErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public int getStatus() {
        return errorResponse != null ? errorResponse.getStatus() : 500;
    }
}