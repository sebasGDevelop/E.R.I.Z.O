package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;

import dev.erizo.firewall.shared.EnemyId;
import dev.erizo.firewall.shared.GridPosition;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class TargetStrategyExtensionTest {

    private static final class WeakestTarget implements TargetStrategy {
        @Override
        public Optional<Enemy> choose(List<Enemy> candidates, GridPosition towerPosition, EnemyPath path) {
            return candidates.stream().min(Comparator.comparingInt(Enemy::health));
        }
    }

    @Test
    void newStrategy_pluggedIntoTower_choosesByItsOwnRule() {
        Enemy strong = Enemy.spawn(new EnemyId(1), EnemyKind.TROJY);
        Enemy weak = Enemy.spawn(new EnemyId(2), EnemyKind.WORMY);

        Optional<Enemy> chosen = new WeakestTarget()
                .choose(List.of(strong, weak), new GridPosition(0, 1), null);

        assertThat(chosen).contains(weak);
    }
}
