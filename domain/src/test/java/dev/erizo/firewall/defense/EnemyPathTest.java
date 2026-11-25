package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.erizo.firewall.shared.GridPosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class EnemyPathTest {

    private static MapLayout mapWithStraightPath() {
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

    private static List<GridPosition> straightSteps() {
        List<GridPosition> steps = new ArrayList<>();
        for (int x = 0; x < MapLayout.WIDTH; x++) {
            steps.add(new GridPosition(x, 0));
        }
        return steps;
    }

    @Test
    void length_whenPathSpansRow_equalsTileGaps() {
        EnemyPath path = EnemyPath.over(mapWithStraightPath(), straightSteps());

        assertThat(path.length()).isEqualTo(MapLayout.WIDTH - 1.0);
    }

    @Test
    void positionAt_whenDistanceInterior_returnsThatTile() {
        EnemyPath path = EnemyPath.over(mapWithStraightPath(), straightSteps());

        assertThat(path.positionAt(3.0)).isEqualTo(new GridPosition(3, 0));
    }

    @Test
    void positionAt_whenDistancePastEnd_returnsNode() {
        EnemyPath path = EnemyPath.over(mapWithStraightPath(), straightSteps());

        assertThat(path.positionAt(999.0)).isEqualTo(path.node());
    }

    @Test
    void over_whenPathDoesNotStartAtSpawn_throws() {
        List<GridPosition> steps = straightSteps();
        steps.remove(0);

        assertThatThrownBy(() -> EnemyPath.over(mapWithStraightPath(), steps))
                .isInstanceOf(InvalidMapException.class);
    }

    @Test
    void reachesNode_whenDistanceAtLength_isTrue() {
        EnemyPath path = EnemyPath.over(mapWithStraightPath(), straightSteps());

        assertThat(path.reachesNode(path.length())).isTrue();
    }

    @Test
    void positionAt_whenDistanceBeforeStart_returnsSpawn() {
        EnemyPath path = EnemyPath.over(mapWithStraightPath(), straightSteps());

        assertThat(path.positionAt(-1.0)).isEqualTo(path.spawn());
    }

    @Test
    void over_whenPathTooShort_throws() {
        List<GridPosition> steps = List.of(new GridPosition(0, 0));

        assertThatThrownBy(() -> EnemyPath.over(mapWithStraightPath(), steps))
                .isInstanceOf(InvalidMapException.class);
    }

    @Test
    void over_whenPathDoesNotEndAtNode_throws() {
        List<GridPosition> steps = straightSteps();
        steps.remove(steps.size() - 1);

        assertThatThrownBy(() -> EnemyPath.over(mapWithStraightPath(), steps))
                .isInstanceOf(InvalidMapException.class);
    }
}
