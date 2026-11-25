package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class NodeIntegrityTest {

    @Test
    void create_whenValueNegative_throws() {
        assertThatThrownBy(() -> new NodeIntegrity(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void subtract_whenLeakNegative_throws() {
        NodeIntegrity integrity = NodeIntegrity.full();

        assertThatThrownBy(() -> integrity.subtract(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void full_atMatchStart_isTwenty() {
        NodeIntegrity integrity = NodeIntegrity.full();

        assertThat(integrity.value()).isEqualTo(20);
    }

    @Test
    void subtract_whenEnemyLeaks_reducesByLeak() {
        NodeIntegrity integrity = NodeIntegrity.full();

        assertThat(integrity.subtract(2).value()).isEqualTo(18);
    }

    @Test
    void subtract_whenLeakExceedsValue_floorsAtZero() {
        NodeIntegrity integrity = new NodeIntegrity(1);

        assertThat(integrity.subtract(5).value()).isEqualTo(0);
    }

    @Test
    void isBreached_whenValueZero_isTrue() {
        NodeIntegrity integrity = new NodeIntegrity(0);

        assertThat(integrity.isBreached()).isTrue();
    }

    @Test
    void isBreached_whenValueAboveZero_isFalse() {
        NodeIntegrity integrity = new NodeIntegrity(1);

        assertThat(integrity.isBreached()).isFalse();
    }
}
