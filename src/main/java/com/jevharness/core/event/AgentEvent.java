package com.jevharness.core.event;

import java.time.Instant;

/**
 * Sealed interface representing all typed events published across an agent execution session.
 */
public sealed interface AgentEvent permits
        AgentStateChangedEvent,
        StepStartedEvent,
        StepCompletedEvent,
        ToolExecutionStartedEvent,
        ToolExecutionCompletedEvent,
        EvaluationCompletedEvent,
        AgentFailedEvent {

    String sessionId();
    Instant timestamp();
}
