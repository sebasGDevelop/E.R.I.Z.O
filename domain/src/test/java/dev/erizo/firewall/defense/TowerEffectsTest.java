package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;

import dev.erizo.firewall.shared.EnemyId;
import dev.erizo.firewall.shared.GridPosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class TowerEffectsTest {

    private final EnemyPath path = straightPath();
    private final DefenseMatch match = DefenseMatch.start(map, path);

    private static MapLayout map = buildMap();

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

    private Enemy enemyAtTile(long id, EnemyKind kind, int tileX) {
        Enemy enemy = Enemy.spawn(new EnemyId(id), kind);
        while (enemy.positionOn(path).x() < tileX) {
            enemy.advance();
        }
        match.addEnemy(enemy);
        return enemy;
    }

    @Test
    void tickTowers_whenSpikeWallReady_dealsDamageToTarget() {
        Tower spike = match.placeTower(TowerKind.SPIKE_WALL, new GridPosition(3, 1));
        Enemy wormy = enemyAtTile(1, EnemyKind.WORMY, 3);

        match.tickTowers();

        assertThat(wormy.health()).isEqualTo(6 - 3);
    }

    @Test
    void tickTowers_whenEnemyOutOfRange_dealsNoDamage() {
        match.placeTower(TowerKind.SPIKE_WALL, new GridPosition(0, 1));
        Enemy wormy = enemyAtTile(1, EnemyKind.WORMY, 8);

        match.tickTowers();

        assertThat(wormy.health()).isEqualTo(6);
    }

    @Test
    void tickTowers_whenEnemyDisguised_spikeWallDoesNotDamageIt() {
        match.placeTower(TowerKind.SPIKE_WALL, new GridPosition(3, 1));
        Enemy phisher = enemyAtTile(1, EnemyKind.PHISHER, 3);

        match.tickTowers();

        assertThat(phisher.health()).isEqualTo(8);
    }

    @Test
    void tickTowers_whenScannerLensScans_revealsDisguisedEnemy() {
        match.placeTower(TowerKind.SCANNER_LENS, new GridPosition(3, 1));
        Enemy phisher = enemyAtTile(1, EnemyKind.PHISHER, 3);

        match.tickTowers();

        assertThat(phisher.canBeTargeted()).isTrue();
    }

    @Test
    void tickTowers_whenScannerLensScans_damagesRevealedEnemy() {
        match.placeTower(TowerKind.SCANNER_LENS, new GridPosition(3, 1));
        Enemy phisher = enemyAtTile(1, EnemyKind.PHISHER, 3);

        match.tickTowers();

        assertThat(phisher.health()).isEqualTo(8 - 1);
    }

    @Test
    void tickTowers_whenHoneypotLures_slowsEnemyInRange() {
        match.placeTower(TowerKind.HONEYPOT, new GridPosition(3, 1));
        Enemy wormy = enemyAtTile(1, EnemyKind.WORMY, 3);

        match.tickTowers();
        double progressBefore = wormy.progress();
        wormy.advance();

        assertThat(wormy.progress() - progressBefore).isLessThan(0.10);
    }

    @Test
    void tickTowers_whenTrafficValveFires_damagesSwarmMember() {
        match.placeTower(TowerKind.TRAFFIC_VALVE, new GridPosition(3, 1));
        Enemy buzz = enemyAtTile(1, EnemyKind.BUZZ_CLOUD_MEMBER, 3);

        match.tickTowers();

        assertThat(buzz.isDisinfected()).isTrue();
    }

    @Test
    void tickTowers_whenSpikeWallOnCooldown_doesNotFireNextTick() {
        match.placeTower(TowerKind.SPIKE_WALL, new GridPosition(3, 1));
        Enemy trojy = enemyAtTile(1, EnemyKind.TROJY, 3);

        match.tickTowers();
        match.tickTowers();

        assertThat(trojy.health()).isEqualTo(10 - 3);
    }

    @Test
    void changeTarget_whenStrategyReplaced_firesByNewStrategy() {
        Tower spike = match.placeTower(TowerKind.SPIKE_WALL, new GridPosition(3, 1));
        Enemy weakAhead = enemyAtTile(1, EnemyKind.WORMY, 4);
        Enemy strongBehind = enemyAtTile(2, EnemyKind.TROJY, 2);
        match.changeTarget(spike, new StrongestTarget());

        match.tickTowers();

        assertThat(strongBehind.health()).isEqualTo(10 - 3);
    }
}
