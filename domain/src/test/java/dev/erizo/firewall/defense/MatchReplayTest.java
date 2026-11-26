package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;

import dev.erizo.firewall.content.WaveDefinition;
import dev.erizo.firewall.content.WaveGroupDefinition;
import dev.erizo.firewall.shared.GridPosition;
import dev.erizo.firewall.shared.Seed;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class MatchReplayTest {

    private static final Seed SEED = new Seed(42);

    private static MapLayout map = buildMap();
    private final EnemyPath path = straightPath();

    private static MapLayout buildMap() {
        List<List<TileType>> rows = new ArrayList<>();
        for (int y = 0; y < MapLayout.HEIGHT; y++) {
            rows.add(new ArrayList<>(Collections.nCopies(MapLayout.WIDTH, TileType.BUILD)));
        }
        for (int x = 0; x < MapLayout.WIDTH; x++) {
            rows.get(0).set(x, TileType.PATH);
        }
        rows.get(0).set(0, TileType.SPAWN);
        rows.get(0).set(MapLayout.WIDTH - 1, TileType.NODE);
        return MapLayout.fromRows(rows);
    }

    private static EnemyPath straightPath() {
        List<GridPosition> steps = new ArrayList<>();
        for (int x = 0; x < MapLayout.WIDTH; x++) {
            steps.add(new GridPosition(x, 0));
        }
        return EnemyPath.over(map, steps);
    }

    private static List<WaveDefinition> oneWormyWave() {
        return List.of(new WaveDefinition(1, List.of(new WaveGroupDefinition("wormy", 2, 30, 0))));
    }

    private CommandLog placeOneSpikeWall() {
        CommandLog log = new CommandLog();
        log.record(new PlaceTowerCommand(0, TowerKind.SPIKE_WALL, new GridPosition(2, 1)));
        return log;
    }

    @Test
    void run_sameSeedWavesAndCommands_reachesSameResult() {
        MatchReplay replay = new MatchReplay(map, path, oneWormyWave());

        MatchResult first = replay.run(SEED, placeOneSpikeWall());
        MatchResult second = replay.run(SEED, placeOneSpikeWall());

        assertThat(first).isEqualTo(second);
    }

    @Test
    void run_whenTowersStopEveryEnemy_clearsTheWave() {
        MatchReplay replay = new MatchReplay(map, path, oneWormyWave());

        MatchResult result = replay.run(SEED, placeOneSpikeWall());

        assertThat(result.wavesCleared()).isEqualTo(1);
    }

    @Test
    void run_whenTowersStopEveryEnemy_keepsNodeIntact() {
        MatchReplay replay = new MatchReplay(map, path, oneWormyWave());

        MatchResult result = replay.run(SEED, placeOneSpikeWall());

        assertThat(result.nodeIntegrity()).isEqualTo(20);
    }

    @Test
    void run_withNoTowers_losesNodeIntegrityToLeaks() {
        MatchReplay replay = new MatchReplay(map, path, oneWormyWave());

        MatchResult result = replay.run(SEED, new CommandLog());

        assertThat(result.nodeIntegrity()).isLessThan(20);
    }

    @Test
    void run_withManyLeaks_endsTheMatchAsLost() {
        List<WaveDefinition> swarm = List.of(
                new WaveDefinition(1, List.of(new WaveGroupDefinition("trojy", 12, 5, 0))));
        MatchReplay replay = new MatchReplay(map, path, swarm);

        MatchResult result = replay.run(SEED, new CommandLog());

        assertThat(result.nodeIntegrity()).isEqualTo(0);
    }
}
