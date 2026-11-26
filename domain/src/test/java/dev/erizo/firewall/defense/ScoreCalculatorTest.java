package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ScoreCalculatorTest {

    private final ScoreCalculator calculator = new ScoreCalculator();

    @Test
    void score_sumsWaveIntegrityBitsAndMissionPoints() {
        long score = calculator.score(3, 20, 50, 2, 0, 1.0);

        assertThat(score).isEqualTo(3 * 500 + 20 * 100 + 50 + 2 * 300);
    }

    @Test
    void score_subtractsTenPerSoldTower() {
        long withoutSales = calculator.score(1, 0, 0, 0, 0, 1.0);
        long withSales = calculator.score(1, 0, 0, 0, 4, 1.0);

        assertThat(withoutSales - withSales).isEqualTo(40);
    }

    @Test
    void score_appliesDailyModifierAndRoundsDown() {
        long score = calculator.score(1, 0, 0, 0, 0, 1.5);

        assertThat(score).isEqualTo((long) Math.floor(500 * 1.5));
    }

    @Test
    void score_roundsDownAfterModifier() {
        long score = calculator.score(0, 0, 1, 0, 0, 0.5);

        assertThat(score).isEqualTo(0);
    }
}
