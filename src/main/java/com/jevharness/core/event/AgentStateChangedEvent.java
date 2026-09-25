package com.jevharness.core.event;

import com.jevharness.core.domain.AgentState;

import java.time.Instant;
import java.util.Objects;

public record AgentStateChangedEvent(
        String sessionId,
        AgentState previousState,
        AgentState newState,
        Instant timestamp
) implements AgentEvent {
    public AgentStateChangedEvent {
        Objects.requireNonNull(sessionId, "sessionId must not be null");
        Objects.requireNonNull(previousState, "previousState must not be null");
        Objects.requireNonNull(newState, "newState must not be null");
        timestamp = timestamp != null ? timestamp : Instant.now();
    }

    public AgentStateChangedEvent(String sessionId, AgentState previousState, AgentState newState) {
        this(sessionId, previousState, newState, Instant.now());
    }
}
