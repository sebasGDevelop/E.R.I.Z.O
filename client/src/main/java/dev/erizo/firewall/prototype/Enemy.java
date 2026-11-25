package dev.erizo.firewall.prototype;

/**
 * Mutable state of one enemy walking the path during the prototype wave.
 */
final class Enemy {
    int id;
    String type;
    EnemyBehavior behavior;
    double integrity;
    double speed;
    int leak;
    int bounty;
    double progress;
    int ageTicks;
    boolean duplicated;
    boolean released;
    boolean disinfected;
    boolean leaked;
}
