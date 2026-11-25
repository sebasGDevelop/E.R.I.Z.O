package dev.erizo.firewall.prototype;

/**
 * Mutable state of one tower placed during the prototype wave.
 */
final class Tower {
    String type;
    TowerBehavior behavior;
    int x;
    int y;
    int cooldownLeft;
}
