package dev.erizo.firewall.defense;

import java.util.ArrayList;
import java.util.List;

/** Fans a match's domain events out to the registered listeners. */
public final class DomainEventPublisher {

    private final List<DomainEventListener> listeners;

    public DomainEventPublisher() {
        this.listeners = new ArrayList<>();
    }

    /** Registers a listener to receive future events. */
    public void register(DomainEventListener listener) {
        listeners.add(listener);
    }

    /** Delivers the event to every registered listener. */
    public void publish(DomainEvent event) {
        for (DomainEventListener listener : listeners) {
            event.dispatchTo(listener);
        }
    }
}
