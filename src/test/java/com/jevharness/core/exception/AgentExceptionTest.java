package com.jevharness.core.exception;

import com.jevharness.core.domain.AgentState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AgentExceptionTest {

    @Test
    @DisplayName("AgentStateException captures current and attempted states")
    void agentStateExceptionDetails() {
        AgentStateException ex = new AgentStateException("Invalid transition", AgentState.INITIALIZING, AgentState.COMPLETED);

        assertThat(ex.getMessage()).isEqualTo("Invalid transition");
        assertThat(ex.getCurrentState()).isEqualTo(AgentState.INITIALIZING);
        assertThat(ex.getAttemptedState()).isEqualTo(AgentState.COMPLETED);
    }

    @Test
    @DisplayName("JevClientException captures HTTP status code if present")
    void jevClientExceptionDetails() {
        JevClientException ex = new JevClientException("Service Unavailable", 503);

        assertThat(ex.getMessage()).isEqualTo("Service Unavailable");
        assertThat(ex.getStatusCode()).isEqualTo(503);
    }

    @Test
    @DisplayName("ToolExecutionException formats tool error message")
    void toolExecutionExceptionDetails() {
        ToolExecutionException ex = new ToolExecutionException("weatherTool", "Timeout connecting to service");

        assertThat(ex.getToolName()).isEqualTo("weatherTool");
        assertThat(ex.getMessage()).isEqualTo("Error executing tool [weatherTool]: Timeout connecting to service");
    }

    @Test
    @DisplayName("GuardrailViolationException captures stage and violation details")
    void guardrailViolationExceptionDetails() {
        GuardrailViolationException ex = new GuardrailViolationException("PRE_INPUT", "Prompt length exceeds 4000 characters");

        assertThat(ex.getStage()).isEqualTo("PRE_INPUT");
        assertThat(ex.getViolationDetails()).isEqualTo("Prompt length exceeds 4000 characters");
        assertThat(ex.getMessage()).isEqualTo("Guardrail violation at stage [PRE_INPUT]: Prompt length exceeds 4000 characters");
    }

    @Test
    @DisplayName("MaxIterationsExceededException captures session ID and max iterations limit")
    void maxIterationsExceededExceptionDetails() {
        MaxIterationsExceededException ex = new MaxIterationsExceededException("session-999", 10);

        assertThat(ex.getMaxIterations()).isEqualTo(10);
        assertThat(ex.getMessage()).isEqualTo("Agent session [session-999] exceeded maximum permitted iterations limit of 10");
    }
}
