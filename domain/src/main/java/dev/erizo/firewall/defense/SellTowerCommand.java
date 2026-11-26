package dev.erizo.firewall.defense;

import dev.erizo.firewall.shared.GridPosition;

/** Sells the tower on a tile at a tick. */
public record SellTowerCommand(int tick, GridPosition position) implements MatchCommand {

    @Override
    public void applyTo(DefenseMatch match) {
        match.sellTowerAt(position);
    }
}
