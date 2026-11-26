package dev.erizo.firewall.defense;

/** Published when an enemy reaches the node and lowers its integrity. */
public record NodeDamaged(int leak, int remainingIntegrity) implements DomainEvent {

    @Override
    public void dispatchTo(DomainEventListener listener) {
        listener.onNodeDamaged(this);
    }
}
