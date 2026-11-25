package dev.erizo.firewall.defense;

import java.util.function.Function;

/** The upgrades a tower can take, each with its cost and how it wraps a profile. */
public enum UpgradeKind {

    EXTENDED_RANGE(ExtendedRange.COST, ExtendedRange::new),
    DOUBLE_SHOT(DoubleShot.COST, DoubleShot::new);

    private final int cost;
    private final Function<TowerProfile, TowerProfile> wrapper;

    UpgradeKind(int cost, Function<TowerProfile, TowerProfile> wrapper) {
        this.cost = cost;
        this.wrapper = wrapper;
    }

    public int cost() {
        return cost;
    }

    /** Wraps the given profile with this upgrade's decoration. */
    public TowerProfile apply(TowerProfile inner) {
        return wrapper.apply(inner);
    }
}
