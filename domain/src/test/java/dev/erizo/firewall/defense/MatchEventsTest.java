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

class MatchEventsTest {

    private static MapLayout map = buildMap();
    private final EnemyPath path = straightPath();

    private static final class CountingListener implements DomainEventListener {
        private int towersPlaced;
        private int enemiesDisinfected;
        private int wavesCleared;
        private int nodeHits;

        @Override
        public void onTowerPlaced(TowerPlaced event) {
            towersPlaced++;
        }

        @Override
        public void onEnemyDisinfected(EnemyDisinfected event) {
            enemiesDisinfected++;
        }

        @Override
        public void onWaveCleared(WaveCleared event) {
            wavesCleared++;
        }

        @Override
        public void onNodeDamaged(NodeDamaged event) {
            nodeHits++;
        }
    }

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

    private MatchResult runWith(CountingListener listener, CommandLog log, List<WaveDefinition> waves) {
        DomainEventPublisher publisher = new DomainEventPublisher();
        publisher.register(listener);
        return new MatchReplay(map, path, waves).run(new Seed(1), log, publisher);
    }

    @Test
    void run_publishesTowerPlacedForEachPlacement() {
        CountingListener listener = new CountingListener();
        CommandLog log = new CommandLog();
        log.record(new PlaceTowerCommand(0, TowerKind.SPIKE_WALL, new GridPosition(2, 1)));
        List<WaveDefinition> waves = List.of(
                new WaveDefinition(1, List.of(new WaveGroupDefinition("wormy", 1, 0, 0))));

        runWith(listener, log, waves);

        assertThat(listener.towersPlaced).isEqualTo(1);
    }

    @Test
    void run_publishesEnemyDisinfectedWhenTowerClearsIt() {
        CountingListener listener = new CountingListener();
        CommandLog log = new CommandLog();
        log.record(new PlaceTowerCommand(0, TowerKind.SPIKE_WALL, new GridPosition(2, 1)));
        List<WaveDefinition> waves = List.of(
                new WaveDefinition(1, List.of(new WaveGroupDefinition("wormy", 1, 0, 0))));

        runWith(listener, log, waves);

        assertThat(listener.enemiesDisinfected).isEqualTo(1);
    }

    @Test
    void run_publishesWaveClearedWhenWaveEnds() {
        CountingListener listener = new CountingListener();
        CommandLog log = new CommandLog();
        log.record(new PlaceTowerCommand(0, TowerKind.SPIKE_WALL, new GridPosition(2, 1)));
        List<WaveDefinition> waves = List.of(
                new WaveDefinition(1, List.of(new WaveGroupDefinition("wormy", 1, 0, 0))));

        runWith(listener, log, waves);

        assertThat(listener.wavesCleared).isEqualTo(1);
    }

    @Test
    void run_publishesNodeDamagedWhenEnemyLeaks() {
        CountingListener listener = new CountingListener();
        List<WaveDefinition> waves = List.of(
                new WaveDefinition(1, List.of(new WaveGroupDefinition("wormy", 1, 0, 0))));

        runWith(listener, new CommandLog(), waves);

        assertThat(listener.nodeHits).isEqualTo(1);
    }

    @Test
    void run_fillsScoreFromFinalState() {
        CommandLog log = new CommandLog();
        log.record(new PlaceTowerCommand(0, TowerKind.SPIKE_WALL, new GridPosition(2, 1)));
        List<WaveDefinition> waves = List.of(
                new WaveDefinition(1, List.of(new WaveGroupDefinition("wormy", 1, 0, 0))));

        MatchResult result = runWith(new CountingListener(), log, waves);

        assertThat(result.score()).isPositive();
    }
}
