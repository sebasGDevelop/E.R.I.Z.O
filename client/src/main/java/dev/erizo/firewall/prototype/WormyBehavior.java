package dev.erizo.firewall.prototype;

import java.io.PrintStream;

/**
 * Wormy stats and its single duplication once it survives long enough.
 */
final class WormyBehavior implements EnemyBehavior {

    private static final int DUPLICATE_TICK = 60;

    private final PrintStream out;

    WormyBehavior(PrintStream out) {
        this.out = out;
    }

    @Override
    public String type() {
        return "WORMY";
    }

    @Override
    public void configure(Enemy enemy) {
        enemy.integrity = 6;
        enemy.speed = 1.0;
        enemy.leak = 1;
        enemy.bounty = 5;
    }

    @Override
    public void applyAbility(Enemy enemy, WaveState state) {
        if (!enemy.duplicated && enemy.ageTicks >= DUPLICATE_TICK) {
            enemy.duplicated = true;
            state.spawnOffspring("WORMY", enemy.progress);
            out.println("DUPLICATE WORMY id=" + enemy.id);
        }
    }
}
