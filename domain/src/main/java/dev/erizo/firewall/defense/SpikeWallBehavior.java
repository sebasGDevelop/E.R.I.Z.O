package dev.erizo.firewall.defense;

import java.util.List;
import java.util.Optional;

/** Deals its damage to a single targeted enemy each cooldown. */
final class SpikeWallBehavior extends CooldownBehavior {

    @Override
    protected boolean fire(Tower tower, List<Enemy> enemiesInRange, EnemyPath path) {
        List<Enemy> targetable = enemiesInRange.stream().filter(Enemy::canBeTargeted).toList();
        Optional<Enemy> target = tower.targetStrategy().choose(targetable, tower.position(), path);
        target.ifPresent(enemy -> enemy.takeDamage(tower.damage()));
        return target.isPresent();
    }
}
