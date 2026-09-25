package com.jevharness.core.event;

import com.jevharness.core.domain.StepRecord;

import java.time.Instant;
import java.util.Objects;

public record StepCompletedEvent(
        String sessionId,
        StepRecord stepRecord,
        Instant timestamp
) implements AgentEvent {
    public StepCompletedEvent {
        Objects.requireNonNull(sessionId, "sessionId must not be null");
        Objects.requireNonNull(stepRecord, "stepRecord must not be null");
        timestamp = timestamp != null ? timestamp : Instant.now();
    }

    public StepCompletedEvent(String sessionId, StepRecord stepRecord) {
        this(sessionId, stepRecord, Instant.now());
    }
}
