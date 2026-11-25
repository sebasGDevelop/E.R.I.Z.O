package dev.erizo.firewall.defense;

/** Adds one extra shot per cooldown on top of the profile it wraps. */
public final class DoubleShot implements TowerProfile {

    public static final int COST = 50;
    private static final int EXTRA_SHOTS = 1;

    private final TowerProfile inner;

    public DoubleShot(TowerProfile inner) {
        this.inner = inner;
    }

    @Override
    public double rangeTiles() {
        return inner.rangeTiles();
    }

    @Override
    public int shotsPerCooldown() {
        return inner.shotsPerCooldown() + EXTRA_SHOTS;
    }
}
