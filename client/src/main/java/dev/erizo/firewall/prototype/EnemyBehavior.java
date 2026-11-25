package dev.erizo.firewall.prototype;

/**
 * Per-type enemy rules shared by every enemy: its name and initial stats.
 */
interface EnemyBehavior {

    String type();

    void configure(Enemy enemy);
}
