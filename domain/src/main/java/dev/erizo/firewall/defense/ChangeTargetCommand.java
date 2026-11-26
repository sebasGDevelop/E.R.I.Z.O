package dev.erizo.firewall.defense;

import dev.erizo.firewall.shared.GridPosition;

/** Changes the targeting strategy of the tower on a tile at a tick. */
public record ChangeTargetCommand(int tick, GridPosition position, TargetStrategyKind strategy)
        implements MatchCommand {

    @Override
    public void applyTo(DefenseMatch match) {
        match.changeTargetAt(position, strategy);
    }
}
