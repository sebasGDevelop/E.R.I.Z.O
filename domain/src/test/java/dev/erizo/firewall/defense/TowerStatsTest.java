package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class TowerStatsTest {

    @Test
    void create_whenRangeNotPositive_throws() {
        assertThatThrownBy(() -> new TowerStats(3, 0.0, 10, 40, 28))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_whenDamageNegative_throws() {
        assertThatThrownBy(() -> new TowerStats(-1, 2.0, 10, 40, 28))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_whenFireRateNegative_throws() {
        assertThatThrownBy(() -> new TowerStats(3, 2.0, -1, 40, 28))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_whenCostNegative_throws() {
        assertThatThrownBy(() -> new TowerStats(3, 2.0, 10, -1, 28))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_whenSellRefundNegative_throws() {
        assertThatThrownBy(() -> new TowerStats(3, 2.0, 10, 40, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
