package dev.erizo.firewall.prototype;

/**
 * Wormy stats and its single duplication once it survives long enough.
 */
final class WormyBehavior implements EnemyBehavior, SpawnsOnEvent {

    private static final int DUPLICATE_TICK = 60;

    private final SimulationOutput output;

    WormyBehavior(SimulationOutput output) {
        this.output = output;
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
            output.line("DUPLICATE WORMY id=" + enemy.id);
        }
    }
}
