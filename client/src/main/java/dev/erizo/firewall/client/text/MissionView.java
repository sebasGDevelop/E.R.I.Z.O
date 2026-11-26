package dev.erizo.firewall.client.text;

import dev.erizo.firewall.missions.MissionResult;
import java.util.List;

/** A passive view that shows a spy mission and its outcome. */
public interface MissionView {

    /** Shows the mission prompt and its numbered options. */
    void showMission(String prompt, List<String> options);

    /** Shows the outcome and its explanation after an answer. */
    void showOutcome(MissionResult result);
}
