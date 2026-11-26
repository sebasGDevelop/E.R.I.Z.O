package dev.erizo.firewall.application;

import dev.erizo.firewall.defense.CommandLog;
import dev.erizo.firewall.defense.DomainEventPublisher;
import dev.erizo.firewall.defense.EnemyPath;
import dev.erizo.firewall.defense.MapLayout;
import dev.erizo.firewall.defense.MatchReplay;
import dev.erizo.firewall.defense.MatchResult;
import dev.erizo.firewall.content.WaveDefinition;
import dev.erizo.firewall.shared.Seed;
import java.util.List;

/** Runs a full match from its seed, waves and commands and returns the result. */
public final class RunMatchUseCase {

    private final MapLayout map;
    private final EnemyPath path;
    private final List<WaveDefinition> waves;

    public RunMatchUseCase(MapLayout map, EnemyPath path, List<WaveDefinition> waves) {
        this.map = map;
        this.path = path;
        this.waves = List.copyOf(waves);
    }

    /** Runs the match, publishing its events through the given publisher. */
    public MatchResult run(Seed seed, CommandLog commandLog, DomainEventPublisher events) {
        return new MatchReplay(map, path, waves).run(seed, commandLog, events);
    }
}
