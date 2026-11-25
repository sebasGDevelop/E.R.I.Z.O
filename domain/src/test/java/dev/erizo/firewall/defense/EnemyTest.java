package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import dev.erizo.firewall.shared.EnemyId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import dev.erizo.firewall.shared.GridPosition;
import org.junit.jupiter.api.Test;

class EnemyTest {

    private static final EnemyId ID = new EnemyId(1);

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

    @Test
    void advance_whenNormal_movesByTileSpeedPerTick() {
        Enemy wormy = Enemy.spawn(ID, EnemyKind.WORMY);

        wormy.advance();

        assertThat(wormy.progress()).isEqualTo(0.10);
    }

    @Test
    void spawn_whenPhisher_startsDisguisedAndUntargetable() {
        Enemy phisher = Enemy.spawn(ID, EnemyKind.PHISHER);

        assertThat(phisher.canBeTargeted()).isFalse();
    }

    @Test
    void reveal_whenDisguised_makesEnemyTargetable() {
        Enemy phisher = Enemy.spawn(ID, EnemyKind.PHISHER);

        phisher.reveal();

        assertThat(phisher.canBeTargeted()).isTrue();
    }

    @Test
    void advance_whenSlowed_movesAtReducedSpeed() {
        Enemy wormy = Enemy.spawn(ID, EnemyKind.WORMY);
        wormy.slow(0.5, 5);

        wormy.advance();

        assertThat(wormy.progress()).isCloseTo(0.05, within(1e-9));
    }

    @Test
    void advance_whenSlowExpires_returnsToFullSpeed() {
        Enemy wormy = Enemy.spawn(ID, EnemyKind.WORMY);
        wormy.slow(0.5, 1);

        wormy.advance();
        wormy.advance();

        assertThat(wormy.progress()).isCloseTo(0.15, within(1e-9));
    }

    @Test
    void takeDamage_whenHealthReachesZero_disinfectsEnemy() {
        Enemy wormy = Enemy.spawn(ID, EnemyKind.WORMY);

        wormy.takeDamage(6);

        assertThat(wormy.isDisinfected()).isTrue();
    }

    @Test
    void advance_whenDisinfected_doesNotMove() {
        Enemy wormy = Enemy.spawn(ID, EnemyKind.WORMY);
        wormy.takeDamage(6);

        wormy.advance();

        assertThat(wormy.progress()).isEqualTo(0.0);
    }

    @Test
    void takeDamage_whenDisinfected_cannotBeTargeted() {
        Enemy wormy = Enemy.spawn(ID, EnemyKind.WORMY);

        wormy.takeDamage(6);

        assertThat(wormy.canBeTargeted()).isFalse();
    }

    @Test
    void hasReachedNode_whenWalkedFullPath_isTrue() {
        EnemyPath path = straightPath();
        Enemy buzz = Enemy.spawn(ID, EnemyKind.BUZZ_CLOUD_MEMBER);
        for (int tick = 0; tick < 100; tick++) {
            buzz.advance();
        }

        assertThat(buzz.hasReachedNode(path)).isTrue();
    }

    @Test
    void leak_forTrojy_matchesNodeDamage() {
        Enemy trojy = Enemy.spawn(ID, EnemyKind.TROJY);

        assertThat(trojy.leak()).isEqualTo(2);
    }

    @Test
    void bounty_forPhisher_matchesBitsReward() {
        Enemy phisher = Enemy.spawn(ID, EnemyKind.PHISHER);

        assertThat(phisher.bounty()).isEqualTo(12);
    }

    @Test
    void positionOn_atSpawn_isPathStart() {
        EnemyPath path = straightPath();
        Enemy wormy = Enemy.spawn(ID, EnemyKind.WORMY);

        assertThat(wormy.positionOn(path)).isEqualTo(path.spawn());
    }

    @Test
    void reveal_whenAlreadyNormal_leavesEnemyTargetable() {
        Enemy wormy = Enemy.spawn(ID, EnemyKind.WORMY);

        wormy.reveal();

        assertThat(wormy.canBeTargeted()).isTrue();
    }

    @Test
    void slow_whenDisinfected_isIgnored() {
        Enemy wormy = Enemy.spawn(ID, EnemyKind.WORMY);
        wormy.takeDamage(6);

        wormy.slow(0.5, 5);

        assertThat(wormy.isDisinfected()).isTrue();
    }

    @Test
    void reveal_whenDisinfected_doesNotMakeTargetable() {
        Enemy phisher = Enemy.spawn(ID, EnemyKind.PHISHER);
        phisher.takeDamage(8);

        phisher.reveal();

        assertThat(phisher.canBeTargeted()).isFalse();
    }

    @Test
    void takeDamage_whenPartial_keepsEnemyActive() {
        Enemy trojy = Enemy.spawn(ID, EnemyKind.TROJY);

        trojy.takeDamage(4);

        assertThat(trojy.health()).isEqualTo(6);
    }

    @Test
    void idAndKind_afterSpawn_matchArguments() {
        Enemy wormy = Enemy.spawn(ID, EnemyKind.WORMY);

        assertThat(List.of(wormy.id(), wormy.kind())).containsExactly(ID, EnemyKind.WORMY);
    }
}
