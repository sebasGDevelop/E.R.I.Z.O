package dev.erizo.firewall.missions;

import java.util.Map;
import java.util.Optional;

/** Decides which mission, if any, is offered during the build time after a wave. */
public final class MissionSchedule {

    private final Map<Integer, Mission> byWave;

    private MissionSchedule(Map<Integer, Mission> byWave) {
        this.byWave = Map.copyOf(byWave);
    }

    /** Offers the first mission after wave 2 and the second after wave 5. */
    public static MissionSchedule forReleaseOne(Mission interceptMessage, Mission sealCheck) {
        return new MissionSchedule(Map.of(2, interceptMessage, 5, sealCheck));
    }

    /** The mission offered after the given cleared wave, if any. */
    public Optional<Mission> offeredAfterWave(int waveNumber) {
        return Optional.ofNullable(byWave.get(waveNumber));
    }
}
