package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class TowerProfileTest {

    private final TowerProfile base = new BaseProfile(TowerKind.SPIKE_WALL.stats());

    @Test
    void base_hasKindRangeAndSingleShot() {
        assertThat(base.rangeTiles()).isEqualTo(2.0);
    }

    @Test
    void extendedRange_addsOneTile() {
        TowerProfile upgraded = new ExtendedRange(base);

        assertThat(upgraded.rangeTiles()).isEqualTo(3.0);
    }

    @Test
    void doubleShot_addsOneShotPerCooldown() {
        TowerProfile upgraded = new DoubleShot(base);

        assertThat(upgraded.shotsPerCooldown()).isEqualTo(2);
    }

    @Test
    void bothUpgrades_composeRangeAndShots() {
        TowerProfile upgraded = new DoubleShot(new ExtendedRange(base));

        assertThat(List.of(upgraded.rangeTiles(), (double) upgraded.shotsPerCooldown()))
                .containsExactly(3.0, 2.0);
    }

    @Test
    void doubleShot_keepsRangeUnchanged() {
        TowerProfile upgraded = new DoubleShot(base);

        assertThat(upgraded.rangeTiles()).isEqualTo(2.0);
    }
}
