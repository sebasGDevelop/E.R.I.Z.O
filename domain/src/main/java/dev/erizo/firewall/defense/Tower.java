package dev.erizo.firewall.defense;

import dev.erizo.firewall.shared.GridPosition;
import dev.erizo.firewall.shared.TowerId;

/** A placed defensive tower occupying one BUILD tile. */
public final class Tower {

    private final TowerId id;
    private final TowerKind kind;
    private final GridPosition position;

    public Tower(TowerId id, TowerKind kind, GridPosition position) {
        this.id = id;
        this.kind = kind;
        this.position = position;
    }

    public TowerId id() {
        return id;
    }

    public TowerKind kind() {
        return kind;
    }

    public GridPosition position() {
        return position;
    }

    public int cost() {
        return kind.cost();
    }
}
