package dev.erizo.firewall.client.text;

import static org.assertj.core.api.Assertions.assertThat;

import dev.erizo.firewall.application.RunMatchUseCase;
import dev.erizo.firewall.content.WaveDefinition;
import dev.erizo.firewall.content.WaveGroupDefinition;
import dev.erizo.firewall.defense.CommandLog;
import dev.erizo.firewall.defense.EnemyPath;
import dev.erizo.firewall.defense.MapLayout;
import dev.erizo.firewall.defense.PlaceTowerCommand;
import dev.erizo.firewall.defense.TileType;
import dev.erizo.firewall.defense.TowerKind;
import dev.erizo.firewall.shared.GridPosition;
import dev.erizo.firewall.shared.Seed;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class MatchPresenterTest {

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

    private MatchPresenter presenter(MapLayout map, TextDebugView view) {
        List<WaveDefinition> waves = List.of(
                new WaveDefinition(1, List.of(new WaveGroupDefinition("wormy", 1, 0, 0))));
        RunMatchUseCase useCase = new RunMatchUseCase(map, straightPath(map), waves);
        return new MatchPresenter(useCase, new MapTextRenderer(), view);
    }

    @Test
    void play_showsTheMapThenTheResult() {
        MapLayout map = buildMap();
        TextDebugView view = new TextDebugView();

        presenter(map, view).play(map, new Seed(1), new CommandLog());

        assertThat(view.lines().get(0)).isEqualTo("MAP");
    }

    @Test
    void play_showsAResultLine() {
        MapLayout map = buildMap();
        TextDebugView view = new TextDebugView();

        presenter(map, view).play(map, new Seed(1), new CommandLog());

        assertThat(view.lines()).anyMatch(line -> line.startsWith("RESULT"));
    }

    @Test
    void play_returnsTheMatchResultFromTheUseCase() {
        MapLayout map = buildMap();
        CommandLog log = new CommandLog();
        log.record(new PlaceTowerCommand(0, TowerKind.SPIKE_WALL, new GridPosition(2, 1)));

        var result = presenter(map, new TextDebugView()).play(map, new Seed(1), log);

        assertThat(result.wavesCleared()).isEqualTo(1);
    }
}
