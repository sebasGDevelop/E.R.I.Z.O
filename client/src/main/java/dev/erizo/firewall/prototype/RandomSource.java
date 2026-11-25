package dev.erizo.firewall.prototype;

/**
 * Source of deterministic random values for the simulation.
 */
public interface RandomSource {

    int nextInt(int bound);
}
