package dev.erizo.firewall.client.text;

import static org.assertj.core.api.Assertions.assertThat;

import dev.erizo.firewall.application.PlayMissionUseCase;
import dev.erizo.firewall.missions.InterceptMessageMission;
import dev.erizo.firewall.missions.Mission;
import org.junit.jupiter.api.Test;

class MissionPresenterTest {

    private final TextDebugView view = new TextDebugView();
    private final MissionPresenter presenter = new MissionPresenter(new PlayMissionUseCase(), view);
    private final Mission mission = new InterceptMessageMission("intercept");

    @Test
    void present_showsThePromptAndOptions() {
        presenter.present(mission);

        assertThat(view.lines().get(0)).startsWith("MISSION");
    }

    @Test
    void answer_whenCorrect_showsASolvedOutcome() {
        presenter.answer(mission, 0);

        assertThat(view.lines()).anyMatch(line -> line.startsWith("OUTCOME solved"));
    }

    @Test
    void answer_whenCorrect_returnsTheResult() {
        var result = presenter.answer(mission, 0);

        assertThat(result.success()).isTrue();
    }

    @Test
    void answer_alwaysShowsTheExplanation() {
        presenter.answer(mission, 1);

        assertThat(view.lines()).anyMatch(line -> line.contains("HTTPS"));
    }
}
