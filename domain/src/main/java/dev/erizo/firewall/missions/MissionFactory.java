package dev.erizo.firewall.missions;

import dev.erizo.firewall.content.MissionDefinition;

/** Creates missions from their content definitions. */
public final class MissionFactory {

    /** Creates the mission described by the given definition, rejecting an unknown type. */
    public Mission create(MissionDefinition definition) {
        return MissionType.fromContentType(definition.type()).build(definition);
    }
}
