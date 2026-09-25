package com.jevharness.core.event;

import java.time.Instant;
import java.util.Objects;

public record AgentFailedEvent(
        String sessionId,
        String errorMessage,
        Throwable cause,
        Instant timestamp
) implements AgentEvent {
    public AgentFailedEvent {
        Objects.requireNonNull(sessionId, "sessionId must not be null");
        Objects.requireNonNull(errorMessage, "errorMessage must not be null");
        timestamp = timestamp != null ? timestamp : Instant.now();
    }

    public AgentFailedEvent(String sessionId, String errorMessage, Throwable cause) {
        this(sessionId, errorMessage, cause, Instant.now());
    }

    public AgentFailedEvent(String sessionId, String errorMessage) {
        this(sessionId, errorMessage, null, Instant.now());
    }
}
