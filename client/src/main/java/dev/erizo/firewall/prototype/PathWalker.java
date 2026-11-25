package dev.erizo.firewall.prototype;

/**
 * The mover's view of an enemy advancing along the path.
 */
interface PathWalker {

    boolean isActive();

    double speed();

    double progress();

    void advance(double tiles);
}
