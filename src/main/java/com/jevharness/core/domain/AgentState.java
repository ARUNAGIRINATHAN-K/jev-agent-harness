package com.jevharness.core.domain;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Represents the finite state machine states of an agent execution lifecycle within the Jev Agent Harness.
 * Enforces valid state transition matrix rules across the Reason-Act-Observe-Evaluate (RAOE) loop.
 */
public enum AgentState {
    INITIALIZING,
    REASONING,
    ACTING,
    OBSERVING,
    EVALUATING,
    COMPLETED,
    FAILED,
    CANCELLED;

    private static final Map<AgentState, Set<AgentState>> ALLOWED_TRANSITIONS = Map.of(
            INITIALIZING, EnumSet.of(REASONING, FAILED, CANCELLED),
            REASONING, EnumSet.of(ACTING, EVALUATING, COMPLETED, FAILED, CANCELLED),
            ACTING, EnumSet.of(OBSERVING, FAILED, CANCELLED),
            OBSERVING, EnumSet.of(REASONING, EVALUATING, FAILED, CANCELLED),
            EVALUATING, EnumSet.of(REASONING, COMPLETED, FAILED, CANCELLED),
            COMPLETED, EnumSet.noneOf(AgentState.class),
            FAILED, EnumSet.noneOf(AgentState.class),
            CANCELLED, EnumSet.noneOf(AgentState.class)
    );

    /**
     * Checks if transitioning from the current state to the target state is allowed.
     *
     * @param targetState The proposed target state.
     * @return true if valid, false otherwise.
     */
    public boolean canTransitionTo(AgentState targetState) {
        if (targetState == null) {
            return false;
        }
        return ALLOWED_TRANSITIONS.getOrDefault(this, Set.of()).contains(targetState);
    }

    /**
     * Determines whether the current state is terminal.
     *
     * @return true if COMPLETED, FAILED, or CANCELLED; false otherwise.
     */
    public boolean isTerminal() {
        return this == COMPLETED || this == FAILED || this == CANCELLED;
    }
}
