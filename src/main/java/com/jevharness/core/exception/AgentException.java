package com.jevharness.core.exception;

/**
 * Base unchecked runtime exception for all errors originating within the Jev Agent Harness.
 */
public class AgentException extends RuntimeException {

    public AgentException(String message) {
        super(message);
    }

    public AgentException(String message, Throwable cause) {
        super(message, cause);
    }
}
