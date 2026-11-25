package dev.erizo.firewall.prototype;

/**
 * Test fake that always returns the same value, keeping runs deterministic.
 */
final class FixedRandomSource implements RandomSource {

    private final int value;

    FixedRandomSource(int value) {
        this.value = value;
    }

    @Override
    public int nextInt(int bound) {
        return value;
    }
}
