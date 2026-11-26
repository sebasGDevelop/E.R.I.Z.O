package dev.erizo.firewall.defense;

/** Something that happened in a match, delivered to interested listeners. */
public interface DomainEvent {

    /** Delivers this event to the listener's matching handler. */
    void dispatchTo(DomainEventListener listener);
}
