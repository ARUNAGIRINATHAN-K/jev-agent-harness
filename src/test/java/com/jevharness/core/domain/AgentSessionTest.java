package com.jevharness.core.domain;

import com.jevharness.core.exception.AgentStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AgentSessionTest {

    @Test
    @DisplayName("Create initial session initializes in INITIALIZING state")
    void createInitialSession() {
        AgentExecutionConfig config = AgentExecutionConfig.defaultConfig();
        AgentSession session = AgentSession.create("weather-agent", config);

        assertThat(session.sessionId()).isNotNull();
        assertThat(session.agentId()).isEqualTo("weather-agent");
        assertThat(session.state()).isEqualTo(AgentState.INITIALIZING);
        assertThat(session.history()).isEmpty();
        assertThat(session.steps()).isEmpty();
        assertThat(session.config()).isEqualTo(config);
    }

    @Test
    @DisplayName("Transition state creates updated session when transition is valid")
    void validStateTransition() {
        AgentSession session = AgentSession.create("test-agent", AgentExecutionConfig.defaultConfig());
        AgentSession reasoningSession = session.transitionTo(AgentState.REASONING);

        assertThat(reasoningSession.state()).isEqualTo(AgentState.REASONING);
        assertThat(reasoningSession.sessionId()).isEqualTo(session.sessionId());
        assertThat(reasoningSession.updatedAt()).isAfterOrEqualTo(session.updatedAt());
    }

    @Test
    @DisplayName("Transition state throws AgentStateException when transition is invalid")
    void invalidStateTransition() {
        AgentSession session = AgentSession.create("test-agent", AgentExecutionConfig.defaultConfig());

        assertThatThrownBy(() -> session.transitionTo(AgentState.COMPLETED))
                .isInstanceOf(AgentStateException.class)
                .hasMessageContaining("Invalid state transition");
    }

    @Test
    @DisplayName("withMessage appends new message to history immutably")
    void appendMessageImmutably() {
        AgentSession session = AgentSession.create("test-agent", AgentExecutionConfig.defaultConfig());
        AgentMessage.UserMessage userMessage = new AgentMessage.UserMessage("Hello Agent");

        AgentSession updatedSession = session.withMessage(userMessage);

        assertThat(session.history()).isEmpty();
        assertThat(updatedSession.history()).hasSize(1);
        assertThat(updatedSession.history().get(0)).isEqualTo(userMessage);
    }

    @Test
    @DisplayName("withStep appends step record to trajectory immutably")
    void appendStepImmutably() {
        AgentSession session = AgentSession.create("test-agent", AgentExecutionConfig.defaultConfig());
        StepRecord step = StepRecord.create(1, AgentState.REASONING, "Thought about user prompt", "No action needed", 150L);

        AgentSession updatedSession = session.withStep(step);

        assertThat(session.steps()).isEmpty();
        assertThat(updatedSession.steps()).hasSize(1);
        assertThat(updatedSession.steps().get(0)).isEqualTo(step);
    }
}
