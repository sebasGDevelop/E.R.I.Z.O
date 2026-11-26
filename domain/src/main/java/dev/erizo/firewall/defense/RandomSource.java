package dev.erizo.firewall.defense;

/** A deterministic source of randomness seeded from the match seed. */
public interface RandomSource {

    /** A non-negative int below the given bound. */
    int nextInt(int bound);
}
