package dev.erizo.firewall.defense;

import dev.erizo.firewall.content.WaveDefinition;
import dev.erizo.firewall.shared.Seed;
import java.util.List;

/** Replays a match from its seed, wave set and command log to a final result. */
public final class MatchReplay {

    private final MapLayout map;
    private final EnemyPath path;
    private final List<WaveDefinition> waveDefinitions;
    private final WaveBuilder waveBuilder;

    public MatchReplay(MapLayout map, EnemyPath path, List<WaveDefinition> waveDefinitions) {
        this.map = map;
        this.path = path;
        this.waveDefinitions = List.copyOf(waveDefinitions);
        this.waveBuilder = new WaveBuilder();
    }

    /** Runs the whole match deterministically and returns its result. */
    public MatchResult run(Seed seed, CommandLog commandLog) {
        DefenseMatch match = DefenseMatch.start(map, path);
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

        return new MatchResult(
                schedule.wavesCleared(),
                match.nodeIntegrity().value(),
                match.bits().amount(),
                0L);
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
