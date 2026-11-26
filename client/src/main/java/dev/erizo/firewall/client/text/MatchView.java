package dev.erizo.firewall.client.text;

import dev.erizo.firewall.defense.MatchResult;
import java.util.List;

/** A passive view that shows a match's map and final result. */
public interface MatchView {

    /** Shows the map grid as the given lines of text. */
    void showMap(List<String> mapRows);

    /** Shows the match's final tallies. */
    void showResult(MatchResult result);
}
