package com.jevharness.core.exception;

import com.jevharness.core.domain.AgentState;

/**
 * Exception thrown when an illegal state transition is attempted on an AgentSession.
 */
public class AgentStateException extends AgentException {

    private final AgentState currentState;
    private final AgentState attemptedState;

    public AgentStateException(String message, AgentState currentState, AgentState attemptedState) {
        super(message);
        this.currentState = currentState;
        this.attemptedState = attemptedState;
    }

    public AgentState getCurrentState() {
        return currentState;
    }

    public AgentState getAttemptedState() {
        return attemptedState;
    }
}
