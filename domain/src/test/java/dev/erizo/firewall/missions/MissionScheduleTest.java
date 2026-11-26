package dev.erizo.firewall.missions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MissionScheduleTest {

    private final Mission intercept = new InterceptMessageMission("intercept");
    private final Mission seal = new SealCheckMission("seal", 1);
    private final MissionSchedule schedule = MissionSchedule.forReleaseOne(intercept, seal);

    @Test
    void offeredAfterWave_two_isTheInterceptMission() {
        assertThat(schedule.offeredAfterWave(2)).contains(intercept);
    }

    @Test
    void offeredAfterWave_five_isTheSealMission() {
        assertThat(schedule.offeredAfterWave(5)).contains(seal);
    }

    @Test
    void offeredAfterWave_otherWave_offersNothing() {
        assertThat(schedule.offeredAfterWave(3)).isEmpty();
    }
}
