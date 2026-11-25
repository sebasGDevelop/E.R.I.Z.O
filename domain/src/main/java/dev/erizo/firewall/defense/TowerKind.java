package dev.erizo.firewall.defense;

/** The four tower kinds, each with its base stats. */
public enum TowerKind {

    SPIKE_WALL(new TowerStats(3, 2.0, 10, 40, 28)),
    SCANNER_LENS(new TowerStats(1, 3.0, 15, 60, 42)),
    HONEYPOT(new TowerStats(0, 2.0, 0, 50, 35)),
    TRAFFIC_VALVE(new TowerStats(1, 2.5, 20, 70, 49));

    private final TowerStats stats;

    TowerKind(TowerStats stats) {
        this.stats = stats;
    }

    public TowerStats stats() {
        return stats;
    }

    public int cost() {
        return stats.cost();
    }
}
