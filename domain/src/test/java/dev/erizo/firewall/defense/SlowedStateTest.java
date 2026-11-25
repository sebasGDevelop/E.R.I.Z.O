package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class SlowedStateTest {

    @Test
    void create_whenMultiplierOutOfRange_throws() {
        assertThatThrownBy(() -> new SlowedState(1.0, 5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_whenDurationNotPositive_throws() {
        assertThatThrownBy(() -> new SlowedState(0.5, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void afterTick_whenTicksRemain_staysSlowed() {
        SlowedState slowed = new SlowedState(0.5, 3);

        EnemyState next = slowed.afterTick();

        assertThat(next.speedMultiplier()).isEqualTo(0.5);
    }

    @Test
    void afterTick_whenLastTick_returnsToNormalSpeed() {
        SlowedState slowed = new SlowedState(0.5, 1);

        EnemyState next = slowed.afterTick();

        assertThat(next.speedMultiplier()).isEqualTo(1.0);
    }
}
