package dev.erizo.firewall.defense;

/** Adds one tile of range on top of the profile it wraps. */
public final class ExtendedRange implements TowerProfile {

    public static final int COST = 30;
    private static final double RANGE_BONUS = 1.0;

    private final TowerProfile inner;

    public ExtendedRange(TowerProfile inner) {
        this.inner = inner;
    }

    @Override
    public double rangeTiles() {
        return inner.rangeTiles() + RANGE_BONUS;
    }

    @Override
    public int shotsPerCooldown() {
        return inner.shotsPerCooldown();
    }
}
