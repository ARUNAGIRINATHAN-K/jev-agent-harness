package com.jevharness.core.event;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Objects;

/**
 * In-memory thread-safe implementation of {@link AgentEventBus} backed by Project Reactor Sinks.
 */
@Component
public class InMemoryAgentEventBus implements AgentEventBus {

    private final Sinks.Many<AgentEvent> eventSink;

    public InMemoryAgentEventBus() {
        this.eventSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @Override
    public void publish(AgentEvent event) {
        Objects.requireNonNull(event, "event must not be null");
        eventSink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<AgentEvent> events() {
        return eventSink.asFlux();
    }

    @Override
    public Flux<AgentEvent> eventsForSession(String sessionId) {
        Objects.requireNonNull(sessionId, "sessionId must not be null");
        return events().filter(e -> sessionId.equals(e.sessionId()));
    }

    @Override
    public <T extends AgentEvent> Flux<T> eventsOfType(Class<T> eventType) {
        Objects.requireNonNull(eventType, "eventType must not be null");
        return events().ofType(eventType);
    }
}
