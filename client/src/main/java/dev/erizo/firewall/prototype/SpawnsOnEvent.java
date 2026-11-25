package dev.erizo.firewall.prototype;

/**
 * An enemy that releases offspring when a trigger is reached.
 */
interface SpawnsOnEvent {

    void applyAbility(Enemy enemy, WaveState state);
}
