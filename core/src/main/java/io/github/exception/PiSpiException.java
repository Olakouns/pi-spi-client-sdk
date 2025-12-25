package io.github.exception;


public class PiSpiException extends RuntimeException {
    public PiSpiException(String message) {
        super(message);
    }

    public PiSpiException(String message, Throwable cause) {
        super(message, cause);
    }
}