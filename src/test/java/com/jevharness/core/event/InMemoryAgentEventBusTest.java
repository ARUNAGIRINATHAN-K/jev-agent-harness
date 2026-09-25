package com.jevharness.core.event;

import com.jevharness.core.domain.AgentState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.time.Duration;

class InMemoryAgentEventBusTest {

    private InMemoryAgentEventBus eventBus;

    @BeforeEach
    void setUp() {
        eventBus = new InMemoryAgentEventBus();
    }

    @Test
    @DisplayName("Publish event dispatches event to active reactive subscribers")
    void publishAndSubscribeEvents() {
        AgentStateChangedEvent event = new AgentStateChangedEvent("session-123", AgentState.INITIALIZING, AgentState.REASONING);

        StepVerifier.create(eventBus.events())
                .then(() -> eventBus.publish(event))
                .expectNext(event)
                .thenCancel()
                .verify(Duration.ofSeconds(2));
    }

    @Test
    @DisplayName("eventsForSession filters events matching target session ID")
    void eventsForSessionFiltering() {
        AgentStateChangedEvent event1 = new AgentStateChangedEvent("session-A", AgentState.INITIALIZING, AgentState.REASONING);
        AgentStateChangedEvent event2 = new AgentStateChangedEvent("session-B", AgentState.INITIALIZING, AgentState.REASONING);
        AgentStateChangedEvent event3 = new AgentStateChangedEvent("session-A", AgentState.REASONING, AgentState.ACTING);

        StepVerifier.create(eventBus.eventsForSession("session-A"))
                .then(() -> {
                    eventBus.publish(event1);
                    eventBus.publish(event2);
                    eventBus.publish(event3);
                })
                .expectNext(event1, event3)
                .thenCancel()
                .verify(Duration.ofSeconds(2));
    }

    @Test
    @DisplayName("eventsOfType filters events matching target event class")
    void eventsOfTypeFiltering() {
        AgentStateChangedEvent stateEvent = new AgentStateChangedEvent("session-1", AgentState.INITIALIZING, AgentState.REASONING);
        StepStartedEvent stepEvent = new StepStartedEvent("session-1", 1);

        StepVerifier.create(eventBus.eventsOfType(StepStartedEvent.class))
                .then(() -> {
                    eventBus.publish(stateEvent);
                    eventBus.publish(stepEvent);
                })
                .expectNext(stepEvent)
                .thenCancel()
                .verify(Duration.ofSeconds(2));
    }
}
