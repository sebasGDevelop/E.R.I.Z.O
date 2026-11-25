package dev.erizo.firewall.defense;

import dev.erizo.firewall.shared.GridPosition;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/** Targets the enemy nearest the tower, breaking ties by lowest id. */
public final class ClosestTarget implements TargetStrategy {

    @Override
    public Optional<Enemy> choose(List<Enemy> candidates, GridPosition towerPosition, EnemyPath path) {
        return candidates.stream()
                .min(Comparator.comparingDouble((Enemy enemy) -> towerPosition.distanceTo(enemy.positionOn(path)))
                        .thenComparing(enemy -> enemy.id().value()));
    }
}
