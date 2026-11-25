package dev.erizo.firewall.defense;

/** The four tower kinds, each with its base stats. */
public enum TowerKind {

    SPIKE_WALL(new TowerStats(3, 2.0, 10, 40, 28), new SpikeWallBehavior()),
    SCANNER_LENS(new TowerStats(1, 3.0, 15, 60, 42), new ScannerLensBehavior()),
    HONEYPOT(new TowerStats(0, 2.0, 0, 50, 35), new HoneypotBehavior()),
    TRAFFIC_VALVE(new TowerStats(1, 2.5, 20, 70, 49), new TrafficValveBehavior());

    private final TowerStats stats;
    private final TowerBehavior behavior;

    TowerKind(TowerStats stats, TowerBehavior behavior) {
        this.stats = stats;
        this.behavior = behavior;
    }

    public TowerStats stats() {
        return stats;
    }

    public TowerBehavior behavior() {
        return behavior;
    }

    public int cost() {
        return stats.cost();
    }
}
