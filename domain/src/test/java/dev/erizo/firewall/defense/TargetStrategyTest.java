package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;

import dev.erizo.firewall.shared.EnemyId;
import dev.erizo.firewall.shared.GridPosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class TargetStrategyTest {

    private static EnemyPath straightPath() {
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
        return EnemyPath.over(map, steps);
    }

    private static Enemy walked(long id, EnemyKind kind, int ticks) {
        Enemy enemy = Enemy.spawn(new EnemyId(id), kind);
        for (int tick = 0; tick < ticks; tick++) {
            enemy.advance();
        }
        return enemy;
    }

    @Test
    void firstTarget_picksEnemyFarthestAlongPath() {
        Enemy behind = walked(1, EnemyKind.WORMY, 5);
        Enemy ahead = walked(2, EnemyKind.WORMY, 20);

        Enemy chosen = new FirstTarget()
                .choose(List.of(behind, ahead), new GridPosition(0, 1), straightPath()).orElseThrow();

        assertThat(chosen).isEqualTo(ahead);
    }

    @Test
    void strongestTarget_picksEnemyWithHighestHealth() {
        Enemy wormy = Enemy.spawn(new EnemyId(1), EnemyKind.WORMY);
        Enemy trojy = Enemy.spawn(new EnemyId(2), EnemyKind.TROJY);

        Enemy chosen = new StrongestTarget()
                .choose(List.of(wormy, trojy), new GridPosition(0, 1), straightPath()).orElseThrow();

        assertThat(chosen).isEqualTo(trojy);
    }

    @Test
    void closestTarget_picksEnemyNearestTheTower() {
        Enemy near = walked(1, EnemyKind.WORMY, 10);
        Enemy far = walked(2, EnemyKind.WORMY, 60);

        Enemy chosen = new ClosestTarget()
                .choose(List.of(near, far), new GridPosition(1, 0), straightPath()).orElseThrow();

        assertThat(chosen).isEqualTo(near);
    }

    @Test
    void firstTarget_whenProgressTied_breaksByLowestId() {
        Enemy higherId = walked(9, EnemyKind.WORMY, 10);
        Enemy lowerId = walked(2, EnemyKind.WORMY, 10);

        Enemy chosen = new FirstTarget()
                .choose(List.of(higherId, lowerId), new GridPosition(0, 1), straightPath()).orElseThrow();

        assertThat(chosen).isEqualTo(lowerId);
    }

    @Test
    void choose_whenNoCandidates_isEmpty() {
        assertThat(new FirstTarget().choose(List.of(), new GridPosition(0, 1), straightPath()))
                .isEmpty();
    }
}
