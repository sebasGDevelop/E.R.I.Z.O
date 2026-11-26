package dev.erizo.firewall.missions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.erizo.firewall.content.MissionDefinition;
import java.util.List;
import org.junit.jupiter.api.Test;

class MissionFactoryTest {

    private final MissionFactory factory = new MissionFactory();

    @Test
    void create_interceptMessage_buildsThatMission() {
        MissionDefinition definition = new MissionDefinition("m1", "intercept_message", 300, List.of());

        Mission mission = factory.create(definition);

        assertThat(mission).isInstanceOf(InterceptMessageMission.class);
    }

    @Test
    void create_sealCheck_buildsThatMission() {
        MissionDefinition definition = new MissionDefinition("m2", "seal_check", 300, List.of("1"));

        Mission mission = factory.create(definition);

        assertThat(mission).isInstanceOf(SealCheckMission.class);
    }

    @Test
    void create_sealCheck_usesTheConfiguredInvalidLink() {
        MissionDefinition definition = new MissionDefinition("m2", "seal_check", 300, List.of("2"));

        Mission mission = factory.create(definition);

        assertThat(mission.evaluate(2).success()).isTrue();
    }

    @Test
    void create_unknownType_isRejected() {
        MissionDefinition definition = new MissionDefinition("m3", "treasure_hunt", 300, List.of());

        assertThatThrownBy(() -> factory.create(definition))
                .isInstanceOf(UnknownMissionTypeException.class);
    }
}
