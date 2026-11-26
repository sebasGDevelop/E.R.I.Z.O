package dev.erizo.firewall.application;

import static org.assertj.core.api.Assertions.assertThat;

import dev.erizo.firewall.missions.InterceptMessageMission;
import dev.erizo.firewall.missions.MissionResult;
import org.junit.jupiter.api.Test;

class PlayMissionUseCaseTest {

    private final PlayMissionUseCase useCase = new PlayMissionUseCase();

    @Test
    void answer_whenCorrect_reportsCompletion() {
        MissionResult result = useCase.answer(new InterceptMessageMission("intercept"), 0);

        assertThat(result.success()).isTrue();
    }

    @Test
    void answer_whenWrong_reportsFailure() {
        MissionResult result = useCase.answer(new InterceptMessageMission("intercept"), 1);

        assertThat(result.success()).isFalse();
    }
}
