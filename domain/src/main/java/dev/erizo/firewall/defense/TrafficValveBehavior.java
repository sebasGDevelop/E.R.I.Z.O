package dev.erizo.firewall.defense;

import java.util.List;

/** Slows every enemy in range each cooldown and chips swarm members. */
final class TrafficValveBehavior extends CooldownBehavior {

    private static final double SLOW_MULTIPLIER = 0.5;
    private static final int SLOW_TICKS = 15;
    private static final int SWARM_DAMAGE = 1;

    @Override
    protected boolean fire(Tower tower, List<Enemy> enemiesInRange, EnemyPath path) {
        if (enemiesInRange.isEmpty()) {
            return false;
        }
        for (Enemy enemy : enemiesInRange) {
            enemy.slow(SLOW_MULTIPLIER, SLOW_TICKS);
            if (enemy.isSwarmMember()) {
                enemy.takeDamage(SWARM_DAMAGE);
            }
        }
        return true;
    }
}
