package com.jevharness.core.domain;

import com.jevharness.core.exception.AgentStateException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Immutable record representing an active or completed agent execution session.
 *
 * @param sessionId Unique session identifier.
 * @param agentId   Identifier of the agent definition/type executing this session.
 * @param state     Current state machine state of the session.
 * @param config    Execution configuration parameters.
 * @param history   Unmodifiable history of exchanged messages.
 * @param steps     Unmodifiable trajectory of step records.
 * @param metadata  Session-level metadata attributes.
 * @param createdAt Creation timestamp.
 * @param updatedAt Last update timestamp.
 */
public record AgentSession(
        String sessionId,
        String agentId,
        AgentState state,
        AgentExecutionConfig config,
        List<AgentMessage> history,
        List<StepRecord> steps,
        Map<String, Object> metadata,
        Instant createdAt,
        Instant updatedAt
) {
    public AgentSession {
        Objects.requireNonNull(sessionId, "sessionId must not be null");
        Objects.requireNonNull(agentId, "agentId must not be null");
        Objects.requireNonNull(state, "state must not be null");
        Objects.requireNonNull(config, "config must not be null");
        history = history != null ? List.copyOf(history) : List.of();
        steps = steps != null ? List.copyOf(steps) : List.of();
        metadata = metadata != null ? Map.copyOf(metadata) : Map.of();
        createdAt = createdAt != null ? createdAt : Instant.now();
        updatedAt = updatedAt != null ? updatedAt : Instant.now();
    }

    /**
     * Creates a new initial AgentSession.
     *
     * @param agentId Identifies the agent definition.
     * @param config  Configuration parameters.
     * @return Initialized AgentSession in INITIALIZING state.
     */
    public static AgentSession create(String agentId, AgentExecutionConfig config) {
        String id = UUID.randomUUID().toString();
        Instant now = Instant.now();
        return new AgentSession(
                id,
                agentId,
                AgentState.INITIALIZING,
                config,
                List.of(),
                List.of(),
                Map.of(),
                now,
                now
        );
    }

    /**
     * Transition the session to a new state after validating transition rules.
     *
     * @param newState Target state to transition to.
     * @return New AgentSession instance with updated state.
     * @throws AgentStateException if the transition is invalid.
     */
    public AgentSession transitionTo(AgentState newState) {
        if (!state.canTransitionTo(newState)) {
            throw new AgentStateException(
                    String.format("Invalid state transition for session [%s] from %s to %s", sessionId, state, newState),
                    state,
                    newState
            );
        }
        return new AgentSession(
                sessionId,
                agentId,
                newState,
                config,
                history,
                steps,
                metadata,
                createdAt,
                Instant.now()
        );
    }

    /**
     * Appends a message to the session message history.
     *
     * @param message Message to append.
     * @return New AgentSession instance with appended message.
     */
    public AgentSession withMessage(AgentMessage message) {
        Objects.requireNonNull(message, "message must not be null");
        List<AgentMessage> updatedHistory = new ArrayList<>(history);
        updatedHistory.add(message);
        return new AgentSession(
                sessionId,
                agentId,
                state,
                config,
                updatedHistory,
                steps,
                metadata,
                createdAt,
                Instant.now()
        );
    }

    /**
     * Appends a step record to the trajectory.
     *
     * @param step StepRecord to append.
     * @return New AgentSession instance with appended step.
     */
    public AgentSession withStep(StepRecord step) {
        Objects.requireNonNull(step, "step must not be null");
        List<StepRecord> updatedSteps = new ArrayList<>(steps);
        updatedSteps.add(step);
        return new AgentSession(
                sessionId,
                agentId,
                state,
                config,
                history,
                updatedSteps,
                metadata,
                createdAt,
                Instant.now()
        );
    }
}
