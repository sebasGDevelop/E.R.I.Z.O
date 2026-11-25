package dev.erizo.firewall.defense;

/** The four malware kinds, each with its content id, base stats and traits. */
public enum EnemyKind {

    WORMY("wormy", new EnemyStats(6, 0.10, 5, 1), false, false),
    TROJY("trojy", new EnemyStats(10, 0.08, 10, 2), false, false),
    PHISHER("phisher", new EnemyStats(8, 0.12, 12, 2), true, false),
    BUZZ_CLOUD_MEMBER("buzz_cloud", new EnemyStats(1, 0.15, 1, 1), false, true);

    private final String contentId;
    private final EnemyStats stats;
    private final boolean startsDisguised;
    private final boolean swarmMember;

    EnemyKind(String contentId, EnemyStats stats, boolean startsDisguised, boolean swarmMember) {
        this.contentId = contentId;
        this.stats = stats;
        this.startsDisguised = startsDisguised;
        this.swarmMember = swarmMember;
    }

    /** The enemy kind for a content id, rejecting one that is not known. */
    public static EnemyKind fromContentId(String contentId) {
        for (EnemyKind kind : values()) {
            if (kind.contentId.equals(contentId)) {
                return kind;
            }
        }
        throw new UnknownEnemyTypeException("Unknown enemy type: " + contentId);
    }

    public String contentId() {
        return contentId;
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
