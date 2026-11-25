package dev.erizo.firewall.defense;

/** The unupgraded profile taken straight from a tower kind's stats. */
public final class BaseProfile implements TowerProfile {

    private final TowerStats stats;

    public BaseProfile(TowerStats stats) {
        this.stats = stats;
    }

    @Override
    public double rangeTiles() {
        return stats.rangeTiles();
    }

    @Override
    public int shotsPerCooldown() {
        return 1;
    }
}
