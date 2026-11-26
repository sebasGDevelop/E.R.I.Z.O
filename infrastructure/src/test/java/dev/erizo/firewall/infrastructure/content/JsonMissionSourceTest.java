package dev.erizo.firewall.infrastructure.content;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.erizo.firewall.content.MissionDefinition;
import java.util.List;
import org.junit.jupiter.api.Test;

class JsonMissionSourceTest {

    private JsonContentSource source(String missionsPath) {
        return new JsonContentSource("content/test-waves.json", missionsPath);
    }

    @Test
    void loadMissions_readsEveryMission() {
        List<MissionDefinition> missions = source("content/test-missions.json").loadMissions();

        assertThat(missions).hasSize(2);
    }

    @Test
    void loadMissions_mapsTypeFromJson() {
        List<MissionDefinition> missions = source("content/test-missions.json").loadMissions();

        assertThat(missions.get(0).type()).isEqualTo("intercept_message");
    }

    @Test
    void loadMissions_mapsItemsFromJson() {
        List<MissionDefinition> missions = source("content/test-missions.json").loadMissions();

        assertThat(missions.get(1).items()).containsExactly("1");
    }

    @Test
    void loadMissions_whenResourceMissing_isRejected() {
        assertThatThrownBy(() -> source("content/no-missions.json").loadMissions())
                .isInstanceOf(ContentLoadException.class);
    }

    @Test
    void loadMissions_bundledContent_hasTwoMissions() {
        List<MissionDefinition> missions = new JsonContentSource("content/waves.json").loadMissions();

        assertThat(missions).hasSize(2);
    }
}
