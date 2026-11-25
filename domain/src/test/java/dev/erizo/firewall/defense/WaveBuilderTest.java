package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;

import dev.erizo.firewall.content.WaveDefinition;
import dev.erizo.firewall.content.WaveGroupDefinition;
import java.util.List;
import org.junit.jupiter.api.Test;

class WaveBuilderTest {

    private final WaveBuilder builder = new WaveBuilder();
    private final EnemyFactory factory = new EnemyFactory();

    private static WaveDefinition definition(WaveGroupDefinition... groups) {
        return new WaveDefinition(1, List.of(groups));
    }

    @Test
    void build_countsEveryEnemyAcrossGroups() {
        Wave wave = builder.build(definition(
                new WaveGroupDefinition("wormy", 6, 20, 0),
                new WaveGroupDefinition("trojy", 1, 0, 120)));

        assertThat(wave.totalEnemies()).isEqualTo(7);
    }

    @Test
    void spawnAt_releasesEnemyAtGroupDelay() {
        Wave wave = builder.build(definition(new WaveGroupDefinition("wormy", 3, 10, 50)));

        List<Enemy> spawned = wave.spawnAt(50, factory);

        assertThat(spawned).hasSize(1);
    }

    @Test
    void spawnAt_releasesEnemyAtEachInterval() {
        Wave wave = builder.build(definition(new WaveGroupDefinition("wormy", 3, 10, 0)));

        List<Enemy> spawned = wave.spawnAt(20, factory);

        assertThat(spawned).hasSize(1);
    }

    @Test
    void spawnAt_whenNoEnemyDue_releasesNothing() {
        Wave wave = builder.build(definition(new WaveGroupDefinition("wormy", 3, 10, 0)));

        List<Enemy> spawned = wave.spawnAt(5, factory);

        assertThat(spawned).isEmpty();
    }

    @Test
    void spawnAt_createsEnemyOfDefinedType() {
        Wave wave = builder.build(definition(new WaveGroupDefinition("trojy", 1, 0, 0)));

        List<Enemy> spawned = wave.spawnAt(0, factory);

        assertThat(spawned.get(0).kind()).isEqualTo(EnemyKind.TROJY);
    }

    @Test
    void fullySpawned_afterLastSpawnTick_isTrue() {
        Wave wave = builder.build(definition(new WaveGroupDefinition("wormy", 3, 10, 0)));

        assertThat(wave.fullySpawned(20)).isTrue();
    }

    @Test
    void fullySpawned_beforeLastSpawnTick_isFalse() {
        Wave wave = builder.build(definition(new WaveGroupDefinition("wormy", 3, 10, 0)));

        assertThat(wave.fullySpawned(19)).isFalse();
    }

    @Test
    void buildTimeBetweenWaves_isThreeHundredTicks() {
        assertThat(Wave.BUILD_TIME_TICKS).isEqualTo(300);
    }
}
