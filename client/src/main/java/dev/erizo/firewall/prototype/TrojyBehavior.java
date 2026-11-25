package dev.erizo.firewall.prototype;

/**
 * Trojy stats and the release of two Wormy once it passes half the path.
 */
final class TrojyBehavior implements EnemyBehavior, SpawnsOnEvent {

    private final SimulationOutput output;

    TrojyBehavior(SimulationOutput output) {
        this.output = output;
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
            output.line("OPEN TROJY id=" + enemy.id);
        }
    }
}
