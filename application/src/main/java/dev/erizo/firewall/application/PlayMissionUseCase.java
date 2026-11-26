package dev.erizo.firewall.application;

import dev.erizo.firewall.missions.Mission;
import dev.erizo.firewall.missions.MissionResult;

/** Evaluates a player's answer to a spy mission. */
public final class PlayMissionUseCase {

    /** Evaluates the chosen option against the mission and returns the outcome. */
    public MissionResult answer(Mission mission, int chosenOption) {
        return mission.evaluate(chosenOption);
    }
}
