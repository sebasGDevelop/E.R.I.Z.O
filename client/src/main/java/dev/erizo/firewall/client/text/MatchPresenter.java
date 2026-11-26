package dev.erizo.firewall.client.text;

import dev.erizo.firewall.application.RunMatchUseCase;
import dev.erizo.firewall.defense.CommandLog;
import dev.erizo.firewall.defense.DomainEventPublisher;
import dev.erizo.firewall.defense.MapLayout;
import dev.erizo.firewall.defense.MatchResult;
import dev.erizo.firewall.shared.Seed;

/** Drives the match view by running a match through the use case. */
public final class MatchPresenter {

    private final RunMatchUseCase runMatch;
    private final MapTextRenderer mapRenderer;
    private final MatchView view;

    public MatchPresenter(RunMatchUseCase runMatch, MapTextRenderer mapRenderer, MatchView view) {
        this.runMatch = runMatch;
        this.mapRenderer = mapRenderer;
        this.view = view;
    }

    /** Shows the map, runs the match and shows its result. */
    public MatchResult play(MapLayout map, Seed seed, CommandLog commandLog) {
        view.showMap(mapRenderer.render(map));
        MatchResult result = runMatch.run(seed, commandLog, new DomainEventPublisher());
        view.showResult(result);
        return result;
    }
}
