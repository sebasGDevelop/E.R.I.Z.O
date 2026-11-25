package dev.erizo.firewall.defense;

import dev.erizo.firewall.shared.GridPosition;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/** Targets the enemy with the highest integrity, breaking ties by lowest id. */
public final class StrongestTarget implements TargetStrategy {

    @Override
    public Optional<Enemy> choose(List<Enemy> candidates, GridPosition towerPosition, EnemyPath path) {
        return candidates.stream()
                .max(Comparator.comparingInt(Enemy::health)
                        .thenComparing(enemy -> enemy.id().value(), Comparator.reverseOrder()));
    }
}
