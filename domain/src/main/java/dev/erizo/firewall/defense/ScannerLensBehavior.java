package dev.erizo.firewall.defense;

import java.util.List;
import java.util.Optional;

/** Reveals disguised enemies in range and deals its damage to a target each cooldown. */
final class ScannerLensBehavior extends CooldownBehavior {

    @Override
    protected boolean fire(Tower tower, List<Enemy> enemiesInRange, EnemyPath path) {
        if (enemiesInRange.isEmpty()) {
            return false;
        }
        enemiesInRange.forEach(Enemy::reveal);
        List<Enemy> targetable = enemiesInRange.stream().filter(Enemy::canBeTargeted).toList();
        Optional<Enemy> target = tower.targetStrategy().choose(targetable, tower.position(), path);
        target.ifPresent(enemy -> enemy.takeDamage(tower.damage()));
        return true;
    }
}
