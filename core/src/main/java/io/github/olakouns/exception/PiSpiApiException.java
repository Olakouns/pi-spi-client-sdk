/*
 * Copyright 2025 Razacki KOUNASSO
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.olakouns.exception;

import io.github.olakouns.representation.ApiErrorResponse;

/**
 * Exception thrown when the PI-SPI API returns an error response.
 * <p>
 * This exception encapsulates the {@link ApiErrorResponse} returned by the server,
 * providing access to specific error codes, messages, and the HTTP status code.
 * </p>
 * * @author Razacki KOUNASSO
 * @since 1.0.0
 */
public class PiSpiApiException extends RuntimeException {
    private final ApiErrorResponse errorResponse;

    public PiSpiApiException(String message, ApiErrorResponse errorResponse) {
        super(message);
        this.errorResponse = errorResponse;
    }

    /**
     * Returns the full error response object containing API-specific error details.
     *
     * @return the {@link ApiErrorResponse} instance, or {@code null} if not available.
     */
    public ApiErrorResponse getErrorResponse() {
        return errorResponse;
    }

    /**
     * Retrieves the HTTP status code associated with this error.
     * <p>
     * Defaults to {@code 500} (Internal Server Error) if the error response is missing.
     * </p>
     *
     * @return the HTTP status code (e.g., 400, 401, 404, 500).
     */
    public int getStatus() {
        return errorResponse != null ? errorResponse.getStatus() : 500;
    }
}