package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.erizo.firewall.shared.EnemyId;
import dev.erizo.firewall.shared.GridPosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class DefenseMatchTest {

    private static final GridPosition BUILD_TILE = new GridPosition(5, 4);
    private static final GridPosition PATH_TILE = new GridPosition(0, 0);

    private static DefenseMatch startedMatch() {
        List<List<TileType>> rows = new ArrayList<>();
        for (int y = 0; y < MapLayout.HEIGHT; y++) {
            rows.add(new ArrayList<>(Collections.nCopies(MapLayout.WIDTH, TileType.BUILD)));
        }
        for (int x = 0; x < MapLayout.WIDTH; x++) {
            rows.get(0).set(x, TileType.PATH);
        }
        rows.get(0).set(0, TileType.SPAWN);
        rows.get(0).set(MapLayout.WIDTH - 1, TileType.NODE);
        MapLayout map = MapLayout.fromRows(rows);
        List<GridPosition> steps = new ArrayList<>();
        for (int x = 0; x < MapLayout.WIDTH; x++) {
            steps.add(new GridPosition(x, 0));
        }
        return DefenseMatch.start(map, EnemyPath.over(map, steps));
    }

    @Test
    void placeTower_onBuildTileWithBits_createsTower() {
        DefenseMatch match = startedMatch();

        Tower tower = match.placeTower(TowerKind.SPIKE_WALL, BUILD_TILE);

        assertThat(tower.position()).isEqualTo(BUILD_TILE);
    }

    @Test
    void placeTower_onBuildTile_subtractsCostFromBits() {
        DefenseMatch match = startedMatch();

        match.placeTower(TowerKind.SPIKE_WALL, BUILD_TILE);

        assertThat(match.bits().amount()).isEqualTo(100 - 40);
    }

    @Test
    void placeTower_onNonBuildTile_isRejected() {
        DefenseMatch match = startedMatch();

        assertThatThrownBy(() -> match.placeTower(TowerKind.SPIKE_WALL, PATH_TILE))
                .isInstanceOf(InvalidPlacementException.class);
    }

    @Test
    void placeTower_onOccupiedTile_isRejected() {
        DefenseMatch match = startedMatch();
        match.placeTower(TowerKind.SPIKE_WALL, BUILD_TILE);

        assertThatThrownBy(() -> match.placeTower(TowerKind.SCANNER_LENS, BUILD_TILE))
                .isInstanceOf(InvalidPlacementException.class);
    }

    @Test
    void placeTower_withoutEnoughBits_isRejected() {
        DefenseMatch match = startedMatch();
        match.placeTower(TowerKind.TRAFFIC_VALVE, new GridPosition(1, 4));

        assertThatThrownBy(() -> match.placeTower(TowerKind.TRAFFIC_VALVE, BUILD_TILE))
                .isInstanceOf(NotEnoughBitsException.class);
    }

    @Test
    void rewardDisinfect_whenEnemyDisinfected_addsBounty() {
        DefenseMatch match = startedMatch();
        Enemy wormy = Enemy.spawn(new EnemyId(1), EnemyKind.WORMY);

        match.rewardDisinfect(wormy);

        assertThat(match.bits().amount()).isEqualTo(100 + 5);
    }

    @Test
    void towerAt_afterPlacement_findsTower() {
        DefenseMatch match = startedMatch();
        Tower placed = match.placeTower(TowerKind.HONEYPOT, BUILD_TILE);

        assertThat(match.towerAt(BUILD_TILE)).contains(placed);
    }

    @Test
    void placeTower_twice_assignsDistinctIds() {
        DefenseMatch match = startedMatch();

        Tower first = match.placeTower(TowerKind.SPIKE_WALL, BUILD_TILE);
        Tower second = match.placeTower(TowerKind.SPIKE_WALL, new GridPosition(6, 4));

        assertThat(second.id()).isNotEqualTo(first.id());
    }
}
