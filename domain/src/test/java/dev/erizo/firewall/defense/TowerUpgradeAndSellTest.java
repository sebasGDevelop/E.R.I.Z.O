package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.erizo.firewall.shared.EnemyId;
import dev.erizo.firewall.shared.GridPosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class TowerUpgradeAndSellTest {

    private static final GridPosition TOWER_TILE = new GridPosition(3, 1);

    private static MapLayout map = buildMap();
    private final EnemyPath path = straightPath();
    private final DefenseMatch match = DefenseMatch.start(map, path);

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
    void upgradeTower_withExtendedRange_subtractsUpgradeCost() {
        Tower spike = match.placeTower(TowerKind.SPIKE_WALL, TOWER_TILE);

        match.upgradeTower(spike, UpgradeKind.EXTENDED_RANGE);

        assertThat(match.bits().amount()).isEqualTo(100 - 40 - 30);
    }

    @Test
    void upgradeTower_withExtendedRange_extendsFiringRange() {
        Tower spike = match.placeTower(TowerKind.SPIKE_WALL, TOWER_TILE);
        Enemy wormy = Enemy.spawn(new EnemyId(1), EnemyKind.WORMY);
        while (wormy.positionOn(path).x() < 1) {
            wormy.advance();
        }
        match.addEnemy(wormy);
        boolean coveredBefore = spike.covers(wormy, path);

        match.upgradeTower(spike, UpgradeKind.EXTENDED_RANGE);

        assertThat(List.of(coveredBefore, spike.covers(wormy, path))).containsExactly(false, true);
    }

    @Test
    void upgradeTower_thirdUpgrade_isRejected() {
        Tower spike = match.placeTower(TowerKind.SPIKE_WALL, TOWER_TILE);
        match.upgradeTower(spike, UpgradeKind.EXTENDED_RANGE);
        match.upgradeTower(spike, UpgradeKind.EXTENDED_RANGE);

        assertThatThrownBy(() -> match.upgradeTower(spike, UpgradeKind.EXTENDED_RANGE))
                .isInstanceOf(UpgradeLimitReachedException.class);
    }

    @Test
    void upgradeTower_withoutEnoughBits_isRejected() {
        Tower valve = match.placeTower(TowerKind.TRAFFIC_VALVE, TOWER_TILE);
        match.upgradeTower(valve, UpgradeKind.EXTENDED_RANGE);

        assertThatThrownBy(() -> match.upgradeTower(valve, UpgradeKind.DOUBLE_SHOT))
                .isInstanceOf(NotEnoughBitsException.class);
    }

    @Test
    void sellTower_removesTowerFromField() {
        Tower spike = match.placeTower(TowerKind.SPIKE_WALL, TOWER_TILE);

        match.sellTower(spike);

        assertThat(match.towers()).isEmpty();
    }

    @Test
    void sellTower_refundsSeventyPercentOfBaseCost() {
        Tower spike = match.placeTower(TowerKind.SPIKE_WALL, TOWER_TILE);

        match.sellTower(spike);

        assertThat(match.bits().amount()).isEqualTo(100 - 40 + 28);
    }

    @Test
    void sellTower_refundsSeventyPercentOfBaseAndUpgrades() {
        Tower spike = match.placeTower(TowerKind.SPIKE_WALL, TOWER_TILE);
        match.upgradeTower(spike, UpgradeKind.EXTENDED_RANGE);

        match.sellTower(spike);

        assertThat(spike.sellRefund()).isEqualTo((int) Math.floor((40 + 30) * 0.70));
    }

    @Test
    void sellTower_whenNotOnField_isRejected() {
        Tower spike = match.placeTower(TowerKind.SPIKE_WALL, TOWER_TILE);
        match.sellTower(spike);

        assertThatThrownBy(() -> match.sellTower(spike))
                .isInstanceOf(InvalidPlacementException.class);
    }

    @Test
    void doubleShot_firesTwiceInOneCooldown() {
        Tower spike = match.placeTower(TowerKind.SPIKE_WALL, TOWER_TILE);
        Enemy trojy = Enemy.spawn(new EnemyId(1), EnemyKind.TROJY);
        while (trojy.positionOn(path).x() < 3) {
            trojy.advance();
        }
        match.addEnemy(trojy);
        match.upgradeTower(spike, UpgradeKind.DOUBLE_SHOT);

        match.tickTowers();

        assertThat(trojy.health()).isEqualTo(10 - 3 - 3);
    }
}
