package com.jevharness.core.event;

import java.time.Instant;
import java.util.Objects;

public record StepStartedEvent(
        String sessionId,
        int stepNumber,
        Instant timestamp
) implements AgentEvent {
    public StepStartedEvent {
        Objects.requireNonNull(sessionId, "sessionId must not be null");
        if (stepNumber < 1) {
            throw new IllegalArgumentException("stepNumber must be at least 1");
        }
        timestamp = timestamp != null ? timestamp : Instant.now();
    }

    public StepStartedEvent(String sessionId, int stepNumber) {
        this(sessionId, stepNumber, Instant.now());
    }
}
