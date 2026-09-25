package com.jevharness.core.event;

import java.time.Instant;
import java.util.Objects;

public record ToolExecutionCompletedEvent(
        String sessionId,
        String toolName,
        String result,
        boolean success,
        long durationMs,
        Instant timestamp
) implements AgentEvent {
    public ToolExecutionCompletedEvent {
        Objects.requireNonNull(sessionId, "sessionId must not be null");
        Objects.requireNonNull(toolName, "toolName must not be null");
        Objects.requireNonNull(result, "result must not be null");
        timestamp = timestamp != null ? timestamp : Instant.now();
    }

    public ToolExecutionCompletedEvent(String sessionId, String toolName, String result, boolean success, long durationMs) {
        this(sessionId, toolName, result, success, durationMs, Instant.now());
    }
}
