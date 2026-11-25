package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TowerKindTest {

    @Test
    void stats_forSpikeWall_matchGameRules() {
        TowerStats stats = TowerKind.SPIKE_WALL.stats();

        assertThat(stats).isEqualTo(new TowerStats(3, 2.0, 10, 40, 28));
    }

    @Test
    void stats_forTrafficValve_matchGameRules() {
        TowerStats stats = TowerKind.TRAFFIC_VALVE.stats();

        assertThat(stats).isEqualTo(new TowerStats(1, 2.5, 20, 70, 49));
    }

    @Test
    void cost_forScannerLens_matchesStats() {
        assertThat(TowerKind.SCANNER_LENS.cost()).isEqualTo(60);
    }
}
