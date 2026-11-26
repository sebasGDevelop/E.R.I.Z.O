package dev.erizo.firewall.missions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class SealCheckMissionTest {

    private final Mission mission = new SealCheckMission("seal", 1);

    @Test
    void options_showTheThreeChainLinks() {
        assertThat(mission.options()).hasSize(3);
    }

    @Test
    void evaluate_whenInvalidLinkChosen_isCompleted() {
        MissionResult result = mission.evaluate(1);

        assertThat(result.success()).isTrue();
    }

    @Test
    void evaluate_whenValidLinkChosen_fails() {
        MissionResult result = mission.evaluate(0);

        assertThat(result.success()).isFalse();
    }

    @Test
    void evaluate_whenCompleted_rewardsFortyBitsAndNextWaveBonus() {
        MissionResult result = mission.evaluate(1);

        assertThat(result.bitsReward() + result.nextWaveBonus()).isEqualTo(60);
    }

    @Test
    void create_whenInvalidLinkOutOfRange_isRejected() {
        assertThatThrownBy(() -> new SealCheckMission("seal", 3))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
