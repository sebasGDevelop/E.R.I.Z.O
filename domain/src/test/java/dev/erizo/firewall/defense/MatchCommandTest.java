package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;

import dev.erizo.firewall.shared.GridPosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class MatchCommandTest {

    private static final GridPosition TILE = new GridPosition(3, 1);

    private static MapLayout map = buildMap();
    private final DefenseMatch match = DefenseMatch.start(map, straightPath());

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

    @Test
    void placeTowerCommand_placesTheTower() {
        new PlaceTowerCommand(0, TowerKind.SPIKE_WALL, TILE).applyTo(match);

        assertThat(match.towerAt(TILE)).isPresent();
    }

    @Test
    void upgradeTowerCommand_upgradesTheTowerOnTheTile() {
        new PlaceTowerCommand(0, TowerKind.SPIKE_WALL, TILE).applyTo(match);

        new UpgradeTowerCommand(1, TILE, UpgradeKind.DOUBLE_SHOT).applyTo(match);

        assertThat(match.towerAt(TILE).orElseThrow().shotsPerCooldown()).isEqualTo(2);
    }

    @Test
    void sellTowerCommand_removesTheTowerOnTheTile() {
        new PlaceTowerCommand(0, TowerKind.SPIKE_WALL, TILE).applyTo(match);

        new SellTowerCommand(2, TILE).applyTo(match);

        assertThat(match.towers()).isEmpty();
    }

    @Test
    void changeTargetCommand_retargetsTheTowerOnTheTile() {
        new PlaceTowerCommand(0, TowerKind.SPIKE_WALL, TILE).applyTo(match);

        new ChangeTargetCommand(1, TILE, TargetStrategyKind.STRONGEST).applyTo(match);

        assertThat(match.towerAt(TILE).orElseThrow().targetStrategy()).isInstanceOf(StrongestTarget.class);
    }
}
