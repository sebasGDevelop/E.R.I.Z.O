package dev.erizo.firewall.missions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class InterceptMessageMissionTest {

    private final Mission mission = new InterceptMessageMission("intercept");

    @Test
    void options_offerBothCables() {
        assertThat(mission.options()).hasSize(2);
    }

    @Test
    void evaluate_whenHttpChosen_isCompleted() {
        MissionResult result = mission.evaluate(0);

        assertThat(result.success()).isTrue();
    }

    @Test
    void evaluate_whenHttpChosen_rewardsFortyBits() {
        MissionResult result = mission.evaluate(0);

        assertThat(result.bitsReward()).isEqualTo(40);
    }

    @Test
    void evaluate_whenHttpsChosen_fails() {
        MissionResult result = mission.evaluate(1);

        assertThat(result.success()).isFalse();
    }

    @Test
    void evaluate_whenFailed_rewardsNothing() {
        MissionResult result = mission.evaluate(1);

        assertThat(result.bitsReward()).isEqualTo(0);
    }

    @Test
    void evaluate_alwaysExplainsTheAnswer() {
        MissionResult result = mission.evaluate(1);

        assertThat(result.explanation()).contains("HTTPS");
    }
}
