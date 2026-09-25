package com.jevharness.core.domain;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable record capturing a single Reason-Act-Observe-Evaluate (RAOE) step execution within an agent session.
 *
 * @param stepId      Unique identifier for the step.
 * @param stepNumber  Sequential index of the step (1-indexed).
 * @param state       State of the agent during step execution.
 * @param action      Action taken by the agent (e.g. tool call or reasoning output).
 * @param observation Resulting observation from tool execution or environment.
 * @param durationMs  Duration of step execution in milliseconds.
 * @param timestamp   Instant when the step was recorded.
 * @param metadata    Additional step context attributes.
 */
public record StepRecord(
        String stepId,
        int stepNumber,
        AgentState state,
        String action,
        String observation,
        long durationMs,
        Instant timestamp,
        Map<String, Object> metadata
) {
    public StepRecord {
        Objects.requireNonNull(stepId, "stepId must not be null");
        if (stepNumber < 1) {
            throw new IllegalArgumentException("stepNumber must be at least 1");
        }
        Objects.requireNonNull(state, "state must not be null");
        timestamp = timestamp != null ? timestamp : Instant.now();
        metadata = metadata != null ? Map.copyOf(metadata) : Map.of();
    }

    public static StepRecord create(int stepNumber, AgentState state, String action, String observation, long durationMs) {
        return new StepRecord(
                UUID.randomUUID().toString(),
                stepNumber,
                state,
                action,
                observation,
                durationMs,
                Instant.now(),
                Map.of()
        );
    }
}
