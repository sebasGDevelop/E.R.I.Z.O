package dev.erizo.firewall.defense;

import dev.erizo.firewall.shared.GridPosition;
import dev.erizo.firewall.shared.TowerId;

/** Published when a tower is placed on the map. */
public record TowerPlaced(TowerId towerId, TowerKind kind, GridPosition position) implements DomainEvent {

    @Override
    public void dispatchTo(DomainEventListener listener) {
        listener.onTowerPlaced(this);
    }
}
