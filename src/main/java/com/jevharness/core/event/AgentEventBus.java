package com.jevharness.core.event;

import reactor.core.publisher.Flux;

/**
 * Interface defining event publishing and subscription operations for agent lifecycle events.
 */
public interface AgentEventBus {

    /**
     * Publishes an agent event to all active subscribers.
     *
     * @param event The AgentEvent to publish.
     */
    void publish(AgentEvent event);

    /**
     * Obtains a reactive Flux stream of all published events.
     *
     * @return Flux of AgentEvent instances.
     */
    Flux<AgentEvent> events();

    /**
     * Obtains a reactive Flux stream of events filtered by a specific session ID.
     *
     * @param sessionId Session identifier to filter by.
     * @return Flux of matching AgentEvent instances.
     */
    Flux<AgentEvent> eventsForSession(String sessionId);

    /**
     * Obtains a reactive Flux stream of events filtered by a specific event type.
     *
     * @param eventType Event class type to filter by.
     * @param <T>       Event type parameter extending AgentEvent.
     * @return Flux of typed matching event instances.
     */
    <T extends AgentEvent> Flux<T> eventsOfType(Class<T> eventType);
}
