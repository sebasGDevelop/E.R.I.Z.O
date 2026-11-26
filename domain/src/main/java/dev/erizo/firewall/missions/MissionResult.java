package dev.erizo.firewall.missions;

/** The outcome of a spy mission: whether it was solved, its reward and the explanation. */
public record MissionResult(String missionId, boolean success, int bitsReward, int nextWaveBonus, String explanation) {

    public static final int COMPLETION_BITS = 40;
    public static final int NEXT_WAVE_BONUS = 20;

    /** A solved mission: the standard bits reward and next-wave bonus. */
    public static MissionResult completed(String missionId, String explanation) {
        return new MissionResult(missionId, true, COMPLETION_BITS, NEXT_WAVE_BONUS, explanation);
    }

    /** A failed mission: nothing gained, nothing lost. */
    public static MissionResult failed(String missionId, String explanation) {
        return new MissionResult(missionId, false, 0, 0, explanation);
    }
}
