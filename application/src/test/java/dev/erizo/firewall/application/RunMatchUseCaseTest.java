package dev.erizo.firewall.application;

import static org.assertj.core.api.Assertions.assertThat;

import dev.erizo.firewall.content.WaveDefinition;
import dev.erizo.firewall.content.WaveGroupDefinition;
import dev.erizo.firewall.defense.CommandLog;
import dev.erizo.firewall.defense.DomainEventPublisher;
import dev.erizo.firewall.defense.EnemyPath;
import dev.erizo.firewall.defense.MapLayout;
import dev.erizo.firewall.defense.MatchResult;
import dev.erizo.firewall.defense.TileType;
import dev.erizo.firewall.shared.GridPosition;
import dev.erizo.firewall.shared.Seed;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class RunMatchUseCaseTest {

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
    void run_playsTheWavesToCompletion() {
        MapLayout map = buildMap();
        List<WaveDefinition> waves = List.of(
                new WaveDefinition(1, List.of(new WaveGroupDefinition("wormy", 1, 0, 0))));
        RunMatchUseCase useCase = new RunMatchUseCase(map, straightPath(map), waves);

        MatchResult result = useCase.run(new Seed(1), new CommandLog(), new DomainEventPublisher());

        assertThat(result.wavesCleared()).isEqualTo(1);
    }
}
