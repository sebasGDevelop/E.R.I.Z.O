package dev.erizo.firewall.prototype;

/**
 * Per-type enemy rules: initial stats and the ability applied each tick.
 */
interface EnemyBehavior {

    String type();

    void configure(Enemy enemy);

    void applyAbility(Enemy enemy, WaveState state);
}
