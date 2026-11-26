package dev.erizo.firewall.client.text;

import dev.erizo.firewall.application.PlayMissionUseCase;
import dev.erizo.firewall.missions.Mission;
import dev.erizo.firewall.missions.MissionResult;

/** Drives the mission view: shows the mission, then shows the outcome of an answer. */
public final class MissionPresenter {

    private final PlayMissionUseCase playMission;
    private final MissionView view;

    public MissionPresenter(PlayMissionUseCase playMission, MissionView view) {
        this.playMission = playMission;
        this.view = view;
    }

    /** Shows the mission's prompt and options. */
    public void present(Mission mission) {
        view.showMission(mission.prompt(), mission.options());
    }

    /** Evaluates the chosen option and shows the outcome. */
    public MissionResult answer(Mission mission, int chosenOption) {
        MissionResult result = playMission.answer(mission, chosenOption);
        view.showOutcome(result);
        return result;
    }
}
