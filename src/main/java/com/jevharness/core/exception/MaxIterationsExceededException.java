package com.jevharness.core.exception;

/**
 * Exception thrown when an agent execution session exceeds its configured maximum RAOE iteration limit.
 */
public class MaxIterationsExceededException extends AgentException {

    private final int maxIterations;

    public MaxIterationsExceededException(String sessionId, int maxIterations) {
        super(String.format("Agent session [%s] exceeded maximum permitted iterations limit of %d", sessionId, maxIterations));
        this.maxIterations = maxIterations;
    }

    public int getMaxIterations() {
        return maxIterations;
    }
}
