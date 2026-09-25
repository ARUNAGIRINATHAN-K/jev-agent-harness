package com.jevharness.core.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgentStateTest {

    @Test
    @DisplayName("INITIALIZING state allows valid transitions")
    void initializingValidTransitions() {
        assertThat(AgentState.INITIALIZING.canTransitionTo(AgentState.REASONING)).isTrue();
        assertThat(AgentState.INITIALIZING.canTransitionTo(AgentState.FAILED)).isTrue();
        assertThat(AgentState.INITIALIZING.canTransitionTo(AgentState.CANCELLED)).isTrue();
        assertThat(AgentState.INITIALIZING.canTransitionTo(AgentState.ACTING)).isFalse();
        assertThat(AgentState.INITIALIZING.canTransitionTo(AgentState.COMPLETED)).isFalse();
    }

    @Test
    @DisplayName("REASONING state allows valid transitions")
    void reasoningValidTransitions() {
        assertThat(AgentState.REASONING.canTransitionTo(AgentState.ACTING)).isTrue();
        assertThat(AgentState.REASONING.canTransitionTo(AgentState.EVALUATING)).isTrue();
        assertThat(AgentState.REASONING.canTransitionTo(AgentState.COMPLETED)).isTrue();
        assertThat(AgentState.REASONING.canTransitionTo(AgentState.FAILED)).isTrue();
        assertThat(AgentState.REASONING.canTransitionTo(AgentState.CANCELLED)).isTrue();
        assertThat(AgentState.REASONING.canTransitionTo(AgentState.INITIALIZING)).isFalse();
    }

    @Test
    @DisplayName("ACTING state allows valid transitions")
    void actingValidTransitions() {
        assertThat(AgentState.ACTING.canTransitionTo(AgentState.OBSERVING)).isTrue();
        assertThat(AgentState.ACTING.canTransitionTo(AgentState.FAILED)).isTrue();
        assertThat(AgentState.ACTING.canTransitionTo(AgentState.CANCELLED)).isTrue();
        assertThat(AgentState.ACTING.canTransitionTo(AgentState.REASONING)).isFalse();
    }

    @Test
    @DisplayName("OBSERVING state allows valid transitions")
    void observingValidTransitions() {
        assertThat(AgentState.OBSERVING.canTransitionTo(AgentState.REASONING)).isTrue();
        assertThat(AgentState.OBSERVING.canTransitionTo(AgentState.EVALUATING)).isTrue();
        assertThat(AgentState.OBSERVING.canTransitionTo(AgentState.FAILED)).isTrue();
        assertThat(AgentState.OBSERVING.canTransitionTo(AgentState.CANCELLED)).isTrue();
    }

    @Test
    @DisplayName("EVALUATING state allows valid transitions")
    void evaluatingValidTransitions() {
        assertThat(AgentState.EVALUATING.canTransitionTo(AgentState.REASONING)).isTrue();
        assertThat(AgentState.EVALUATING.canTransitionTo(AgentState.COMPLETED)).isTrue();
        assertThat(AgentState.EVALUATING.canTransitionTo(AgentState.FAILED)).isTrue();
        assertThat(AgentState.EVALUATING.canTransitionTo(AgentState.CANCELLED)).isTrue();
    }

    @ParameterizedTest
    @EnumSource(value = AgentState.class, names = {"COMPLETED", "FAILED", "CANCELLED"})
    @DisplayName("Terminal states do not allow any further transitions")
    void terminalStateTransitions(AgentState terminalState) {
        assertThat(terminalState.isTerminal()).isTrue();
        for (AgentState target : AgentState.values()) {
            assertThat(terminalState.canTransitionTo(target)).isFalse();
        }
    }

    @ParameterizedTest
    @EnumSource(value = AgentState.class, names = {"INITIALIZING", "REASONING", "ACTING", "OBSERVING", "EVALUATING"})
    @DisplayName("Non-terminal states return false for isTerminal()")
    void nonTerminalStateCheck(AgentState nonTerminalState) {
        assertThat(nonTerminalState.isTerminal()).isFalse();
    }
}
