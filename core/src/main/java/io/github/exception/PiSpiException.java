package io.github.exception;

import io.github.representation.ApiErrorResponse;

public class PiSpiException extends RuntimeException {
    private final ApiErrorResponse errorResponse;

    public PiSpiException(String message, ApiErrorResponse errorResponse) {
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