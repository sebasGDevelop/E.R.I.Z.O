package dev.erizo.firewall.prototype.adapter;

import dev.erizo.firewall.prototype.RandomSource;
import java.util.Random;

/**
 * Deterministic random source backed by a seeded generator.
 */
public final class SeededRandomSource implements RandomSource {

    private final Random random;

    public SeededRandomSource(long seed) {
        this.random = new Random(seed);
    }

    @Override
    public int nextInt(int bound) {
        return random.nextInt(bound);
    }
}
