package dev.erizo.firewall.defense;

/** Computes a match score from its final tallies and the daily modifier. */
public final class ScoreCalculator {

    private static final int WAVE_POINTS = 500;
    private static final int INTEGRITY_POINTS = 100;
    private static final int BIT_POINTS = 1;
    private static final int MISSION_POINTS = 300;
    private static final int SOLD_PENALTY = 10;

    /** Scores a match, rounding the modified total down. */
    public long score(
            int wavesCleared,
            int nodeIntegrity,
            int bitsLeft,
            int missionsCompleted,
            int towersSold,
            double dailyModifierMultiplier) {
        long base = (long) wavesCleared * WAVE_POINTS
                + (long) nodeIntegrity * INTEGRITY_POINTS
                + (long) bitsLeft * BIT_POINTS
                + (long) missionsCompleted * MISSION_POINTS;
        long penalty = (long) towersSold * SOLD_PENALTY;
        return (long) Math.floor((base - penalty) * dailyModifierMultiplier);
    }
}
