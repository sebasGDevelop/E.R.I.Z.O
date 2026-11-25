package dev.erizo.firewall.prototype;

import java.io.PrintStream;

/**
 * Trojy stats and the release of two Wormy once it passes half the path.
 */
final class TrojyBehavior implements EnemyBehavior {

    private final PrintStream out;

    TrojyBehavior(PrintStream out) {
        this.out = out;
    }

    @Override
    public String type() {
        return "TROJY";
    }

    @Override
    public void configure(Enemy enemy) {
        enemy.integrity = 10;
        enemy.speed = 0.8;
        enemy.leak = 2;
        enemy.bounty = 10;
    }

    @Override
    public void applyAbility(Enemy enemy, WaveState state) {
        if (!enemy.released && enemy.progress >= (state.pathLength() - 1) / 2.0) {
            enemy.released = true;
            state.spawnOffspring("WORMY", enemy.progress);
            state.spawnOffspring("WORMY", enemy.progress);
            out.println("OPEN TROJY id=" + enemy.id);
        }
    }
}
