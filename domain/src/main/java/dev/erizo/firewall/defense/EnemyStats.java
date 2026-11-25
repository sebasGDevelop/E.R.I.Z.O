package dev.erizo.firewall.defense;

/** The fixed combat and movement figures for an enemy kind. */
public record EnemyStats(int health, double speedTilesPerTick, int bitsReward, int nodeDamage) {

    public EnemyStats {
        if (health <= 0) {
            throw new IllegalArgumentException("Health must be positive");
        }
        if (speedTilesPerTick <= 0) {
            throw new IllegalArgumentException("Speed must be positive");
        }
        if (bitsReward < 0) {
            throw new IllegalArgumentException("Bits reward cannot be negative");
        }
        if (nodeDamage < 0) {
            throw new IllegalArgumentException("Node damage cannot be negative");
        }
    }
}
