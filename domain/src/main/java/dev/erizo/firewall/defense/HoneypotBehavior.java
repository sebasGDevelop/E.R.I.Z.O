package dev.erizo.firewall.defense;

import java.util.List;

/** Slows every enemy in range as a continuous lure, without attacking. */
final class HoneypotBehavior implements TowerBehavior {

    private static final double SLOW_MULTIPLIER = 0.7;
    private static final int SLOW_TICKS = 20;

    @Override
    public void act(Tower tower, List<Enemy> enemiesInRange, EnemyPath path) {
        enemiesInRange.forEach(enemy -> enemy.slow(SLOW_MULTIPLIER, SLOW_TICKS));
    }
}
