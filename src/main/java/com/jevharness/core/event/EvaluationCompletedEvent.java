package com.jevharness.core.event;

import java.time.Instant;
import java.util.Objects;

public record EvaluationCompletedEvent(
        String sessionId,
        double score,
        String feedback,
        Instant timestamp
) implements AgentEvent {
    public EvaluationCompletedEvent {
        Objects.requireNonNull(sessionId, "sessionId must not be null");
        Objects.requireNonNull(feedback, "feedback must not be null");
        timestamp = timestamp != null ? timestamp : Instant.now();
    }

    public EvaluationCompletedEvent(String sessionId, double score, String feedback) {
        this(sessionId, score, feedback, Instant.now());
    }
}
