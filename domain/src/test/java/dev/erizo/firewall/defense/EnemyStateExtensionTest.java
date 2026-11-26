package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class EnemyStateExtensionTest {

    private static final class HastenedState implements EnemyState {
        @Override
        public double speedMultiplier() {
            return 1.5;
        }

        @Override
        public boolean canBeTargeted() {
            return true;
        }

        @Override
        public boolean isMoving() {
            return true;
        }

        @Override
        public boolean isDisinfected() {
            return false;
        }

        @Override
        public EnemyState afterTick() {
            return this;
        }
    }

    @Test
    void newEnemyState_isAValidStateWithoutEditingExistingClasses() {
        EnemyState state = new HastenedState();

        assertThat(state.speedMultiplier()).isEqualTo(1.5);
    }

    @Test
    void newEnemyState_honoursTheStateContract() {
        EnemyState state = new HastenedState();

        assertThat(List.of(state.isMoving(), state.canBeTargeted(), state.isDisinfected()))
                .containsExactly(true, true, false);
    }
}
