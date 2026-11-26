package dev.erizo.firewall.content;

import static org.assertj.core.api.Assertions.assertThat;

import dev.erizo.firewall.defense.CommandLog;
import dev.erizo.firewall.defense.EnemyPath;
import dev.erizo.firewall.defense.MapLayout;
import dev.erizo.firewall.defense.MatchReplay;
import dev.erizo.firewall.defense.MatchResult;
import dev.erizo.firewall.defense.TileType;
import dev.erizo.firewall.shared.GridPosition;
import dev.erizo.firewall.shared.Seed;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class WaveSourceExtensionTest {

    private static final class InMemoryWaveSource implements WaveSource {
        @Override
        public List<WaveDefinition> loadWaves() {
            return List.of(new WaveDefinition(1, List.of(new WaveGroupDefinition("wormy", 1, 0, 0))));
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

    private static EnemyPath straightPath(MapLayout map) {
        List<GridPosition> steps = new ArrayList<>();
        for (int x = 0; x < MapLayout.WIDTH; x++) {
            steps.add(new GridPosition(x, 0));
        }
        return EnemyPath.over(map, steps);
    }

    @Test
    void newWaveSource_drivesAMatch_withoutEditingExistingClasses() {
        MapLayout map = buildMap();
        List<WaveDefinition> waves = new InMemoryWaveSource().loadWaves();
        MatchReplay replay = new MatchReplay(map, straightPath(map), waves);

        MatchResult result = replay.run(new Seed(1), new CommandLog());

        assertThat(result.wavesCleared()).isEqualTo(1);
    }
}
