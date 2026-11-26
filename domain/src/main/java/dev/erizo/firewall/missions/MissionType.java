package dev.erizo.firewall.missions;

import dev.erizo.firewall.content.MissionDefinition;
import java.util.function.Function;

/** The mission kinds, each knowing how to build its mission from a definition. */
public enum MissionType {

    INTERCEPT_MESSAGE("intercept_message", definition -> new InterceptMessageMission(definition.id())),
    SEAL_CHECK("seal_check", definition ->
            new SealCheckMission(definition.id(), Integer.parseInt(definition.items().get(0))));

    private final String contentType;
    private final Function<MissionDefinition, Mission> builder;

    MissionType(String contentType, Function<MissionDefinition, Mission> builder) {
        this.contentType = contentType;
        this.builder = builder;
    }

    /** The mission type for a content type string, rejecting one that is not known. */
    public static MissionType fromContentType(String contentType) {
        for (MissionType type : values()) {
            if (type.contentType.equals(contentType)) {
                return type;
            }
        }
        throw new UnknownMissionTypeException("Unknown mission type: " + contentType);
    }

    /** Builds the mission described by the given definition. */
    public Mission build(MissionDefinition definition) {
        return builder.apply(definition);
    }
}
