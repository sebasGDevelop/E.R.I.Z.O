package dev.erizo.firewall.defense;

/** The four malware kinds, each with its base stats and whether it starts disguised. */
public enum EnemyKind {

    WORMY(new EnemyStats(6, 0.10, 5, 1), false, false),
    TROJY(new EnemyStats(10, 0.08, 10, 2), false, false),
    PHISHER(new EnemyStats(8, 0.12, 12, 2), true, false),
    BUZZ_CLOUD_MEMBER(new EnemyStats(1, 0.15, 1, 1), false, true);

    private final EnemyStats stats;
    private final boolean startsDisguised;
    private final boolean swarmMember;

    EnemyKind(EnemyStats stats, boolean startsDisguised, boolean swarmMember) {
        this.stats = stats;
        this.startsDisguised = startsDisguised;
        this.swarmMember = swarmMember;
    }

    public EnemyStats stats() {
        return stats;
    }

    public boolean startsDisguised() {
        return startsDisguised;
    }

    public boolean swarmMember() {
        return swarmMember;
    }
}
