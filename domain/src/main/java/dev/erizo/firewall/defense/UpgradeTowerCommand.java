package dev.erizo.firewall.defense;

import dev.erizo.firewall.shared.GridPosition;

/** Upgrades the tower on a tile at a tick. */
public record UpgradeTowerCommand(int tick, GridPosition position, UpgradeKind upgrade) implements MatchCommand {

    @Override
    public void applyTo(DefenseMatch match) {
        match.upgradeTowerAt(position, upgrade);
    }
}
