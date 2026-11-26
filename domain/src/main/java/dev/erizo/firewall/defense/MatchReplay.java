package dev.erizo.firewall.defense;

import dev.erizo.firewall.content.WaveDefinition;
import dev.erizo.firewall.shared.Seed;
import java.util.List;

/** Replays a match from its seed, wave set and command log to a final result. */
public final class MatchReplay {

    private static final double NORMAL_DAILY_MODIFIER = 1.0;

    private final MapLayout map;
    private final EnemyPath path;
    private final List<WaveDefinition> waveDefinitions;
    private final WaveBuilder waveBuilder;
    private final ScoreCalculator scoreCalculator;

    public MatchReplay(MapLayout map, EnemyPath path, List<WaveDefinition> waveDefinitions) {
        this.map = map;
        this.path = path;
        this.waveDefinitions = List.copyOf(waveDefinitions);
        this.waveBuilder = new WaveBuilder();
        this.scoreCalculator = new ScoreCalculator();
    }

    /** Runs the whole match deterministically, publishing nothing, and returns its result. */
    public MatchResult run(Seed seed, CommandLog commandLog) {
        return run(seed, commandLog, new DomainEventPublisher());
    }

    /** Runs the whole match deterministically through the given publisher. */
    public MatchResult run(Seed seed, CommandLog commandLog, DomainEventPublisher events) {
        DefenseMatch match = DefenseMatch.start(map, path, events);
        EnemyFactory factory = new EnemyFactory();
        WaveSchedule schedule = new WaveSchedule(waveDefinitions, waveBuilder);

        int tick = 0;
        while (!match.isLost() && !isFinished(schedule, match, commandLog, tick)) {
            applyCommands(match, commandLog, tick);
            schedule.spawnDue(match, factory, tick);
            match.tickTowers();
            match.advanceEnemies();
            schedule.advance(match, tick);
            tick++;
        }

        long score = scoreCalculator.score(
                schedule.wavesCleared(),
                match.nodeIntegrity().value(),
                match.bits().amount(),
                0,
                match.towersSold(),
                NORMAL_DAILY_MODIFIER);
        return new MatchResult(
                schedule.wavesCleared(),
                match.nodeIntegrity().value(),
                match.bits().amount(),
                score);
    }

    private boolean isFinished(WaveSchedule schedule, DefenseMatch match, CommandLog commandLog, int tick) {
        return schedule.allWavesCleared()
                && match.activeEnemies().isEmpty()
                && tick > commandLog.lastCommandTick();
    }

    private void applyCommands(DefenseMatch match, CommandLog commandLog, int tick) {
        for (MatchCommand command : commandLog.commandsAt(tick)) {
            command.applyTo(match);
        }
    }
}
