package dev.erizo.firewall.defense;

import dev.erizo.firewall.shared.GridPosition;

/** Places a tower of a kind on a tile at a tick. */
public record PlaceTowerCommand(int tick, TowerKind kind, GridPosition position) implements MatchCommand {

    @Override
    public void applyTo(DefenseMatch match) {
        match.placeTower(kind, position);
    }
}
