package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class EnemyStatsTest {

    @Test
    void create_whenHealthNotPositive_throws() {
        assertThatThrownBy(() -> new EnemyStats(0, 1.0, 5, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_whenSpeedNotPositive_throws() {
        assertThatThrownBy(() -> new EnemyStats(6, 0.0, 5, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_whenBitsRewardNegative_throws() {
        assertThatThrownBy(() -> new EnemyStats(6, 1.0, -1, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_whenNodeDamageNegative_throws() {
        assertThatThrownBy(() -> new EnemyStats(6, 1.0, 5, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
