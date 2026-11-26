package dev.erizo.firewall.client.text;

import dev.erizo.firewall.defense.MatchResult;
import dev.erizo.firewall.missions.MissionResult;
import java.util.ArrayList;
import java.util.List;

/** A passive text view that collects the lines it is given, for debugging. */
public final class TextDebugView implements MatchView, MissionView {

    private final List<String> lines = new ArrayList<>();

    @Override
    public void showMap(List<String> mapRows) {
        lines.add("MAP");
        lines.addAll(mapRows);
    }

    @Override
    public void showResult(MatchResult result) {
        lines.add("RESULT waves=" + result.wavesCleared()
                + " node=" + result.nodeIntegrity()
                + " bits=" + result.bits()
                + " score=" + result.score());
    }

    @Override
    public void showMission(String prompt, List<String> options) {
        lines.add("MISSION " + prompt);
        for (int index = 0; index < options.size(); index++) {
            lines.add(index + ") " + options.get(index));
        }
    }

    @Override
    public void showOutcome(MissionResult result) {
        lines.add("OUTCOME " + (result.success() ? "solved" : "failed")
                + " +" + result.bitsReward() + " bits");
        lines.add(result.explanation());
    }

    /** The lines rendered so far, in order. */
    public List<String> lines() {
        return List.copyOf(lines);
    }
}
