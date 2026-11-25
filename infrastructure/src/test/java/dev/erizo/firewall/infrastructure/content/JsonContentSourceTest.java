package dev.erizo.firewall.infrastructure.content;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.erizo.firewall.content.WaveDefinition;
import java.util.List;
import org.junit.jupiter.api.Test;

class JsonContentSourceTest {

    @Test
    void loadWaves_readsEveryWaveFromFile() {
        JsonContentSource source = new JsonContentSource("content/test-waves.json");

        List<WaveDefinition> waves = source.loadWaves();

        assertThat(waves).hasSize(2);
    }

    @Test
    void loadWaves_ordersWavesByNumber() {
        JsonContentSource source = new JsonContentSource("content/test-waves.json");

        List<WaveDefinition> waves = source.loadWaves();

        assertThat(waves.get(0).number()).isEqualTo(1);
    }

    @Test
    void loadWaves_mapsGroupFieldsFromJson() {
        JsonContentSource source = new JsonContentSource("content/test-waves.json");

        List<WaveDefinition> waves = source.loadWaves();

        assertThat(waves.get(0).groups().get(0).enemyType()).isEqualTo("wormy");
    }

    @Test
    void loadWaves_readsEveryGroupOfAWave() {
        JsonContentSource source = new JsonContentSource("content/test-waves.json");

        List<WaveDefinition> waves = source.loadWaves();

        assertThat(waves.get(0).groups()).hasSize(2);
    }

    @Test
    void loadWaves_whenResourceMissing_isRejected() {
        JsonContentSource source = new JsonContentSource("content/no-such-file.json");

        assertThatThrownBy(source::loadWaves)
                .isInstanceOf(ContentLoadException.class);
    }

    @Test
    void loadWaves_bundledContent_hasEightWaves() {
        JsonContentSource source = new JsonContentSource("content/waves.json");

        List<WaveDefinition> waves = source.loadWaves();

        assertThat(waves).hasSize(8);
    }
}
