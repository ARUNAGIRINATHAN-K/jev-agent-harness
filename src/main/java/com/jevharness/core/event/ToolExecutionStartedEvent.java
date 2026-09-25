package com.jevharness.core.event;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

public record ToolExecutionStartedEvent(
        String sessionId,
        String toolName,
        Map<String, Object> arguments,
        Instant timestamp
) implements AgentEvent {
    public ToolExecutionStartedEvent {
        Objects.requireNonNull(sessionId, "sessionId must not be null");
        Objects.requireNonNull(toolName, "toolName must not be null");
        arguments = arguments != null ? Map.copyOf(arguments) : Map.of();
        timestamp = timestamp != null ? timestamp : Instant.now();
    }

    public ToolExecutionStartedEvent(String sessionId, String toolName, Map<String, Object> arguments) {
        this(sessionId, toolName, arguments, Instant.now());
    }
}
