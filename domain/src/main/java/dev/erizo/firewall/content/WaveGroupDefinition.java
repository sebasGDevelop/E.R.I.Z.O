package dev.erizo.firewall.content;

/** The data for one group within a wave: an enemy type and how it is released. */
public record WaveGroupDefinition(String enemyType, int count, int intervalTicks, int delayTicks) {

    public WaveGroupDefinition {
        if (enemyType == null || enemyType.isBlank()) {
            throw new IllegalArgumentException("Enemy type is required");
        }
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }
        if (intervalTicks < 0) {
            throw new IllegalArgumentException("Interval cannot be negative");
        }
        if (delayTicks < 0) {
            throw new IllegalArgumentException("Delay cannot be negative");
        }
    }
}
