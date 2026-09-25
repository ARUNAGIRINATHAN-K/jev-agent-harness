package com.jevharness.core.exception;

/**
 * Exception thrown when communication with or decision evaluation by the Jev System One decision engine fails.
 */
public class JevClientException extends AgentException {

    private final Integer statusCode;

    public JevClientException(String message) {
        super(message);
        this.statusCode = null;
    }

    public JevClientException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = null;
    }

    public JevClientException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public Integer getStatusCode() {
        return statusCode;
    }
}
