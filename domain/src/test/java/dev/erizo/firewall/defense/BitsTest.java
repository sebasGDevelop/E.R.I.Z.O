package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class BitsTest {

    @Test
    void starting_atMatchStart_isOneHundred() {
        assertThat(Bits.starting().amount()).isEqualTo(100);
    }

    @Test
    void spend_whenAffordable_reducesBalance() {
        Bits bits = Bits.starting();

        assertThat(bits.spend(40).amount()).isEqualTo(60);
    }

    @Test
    void spend_whenCostExceedsBalance_throws() {
        Bits bits = new Bits(30);

        assertThatThrownBy(() -> bits.spend(70))
                .isInstanceOf(NotEnoughBitsException.class);
    }

    @Test
    void add_whenEarningBounty_increasesBalance() {
        Bits bits = new Bits(30);

        assertThat(bits.add(5).amount()).isEqualTo(35);
    }

    @Test
    void canAfford_whenCostWithinBalance_isTrue() {
        Bits bits = new Bits(40);

        assertThat(bits.canAfford(40)).isTrue();
    }

    @Test
    void create_whenNegative_throws() {
        assertThatThrownBy(() -> new Bits(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
