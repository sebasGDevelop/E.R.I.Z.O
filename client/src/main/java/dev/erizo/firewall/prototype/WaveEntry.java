package dev.erizo.firewall.prototype;

/**
 * One group within a wave: an enemy type, how many, their interval and delay.
 */
public record WaveEntry(String enemyType, int count, int intervalTicks, int delayTicks) {
}
