package dev.erizo.firewall.defense;

import dev.erizo.firewall.content.WaveDefinition;
import dev.erizo.firewall.content.WaveGroupDefinition;
import java.util.ArrayList;
import java.util.List;

/** Builds a wave's spawn tree from its content definition. */
public final class WaveBuilder {

    /** Builds the wave described by the given definition. */
    public Wave build(WaveDefinition definition) {
        List<WaveComponent> groups = new ArrayList<>();
        for (WaveGroupDefinition group : definition.groups()) {
            groups.add(buildGroup(group));
        }
        return new Wave(definition.number(), new WaveGroup(groups));
    }

    private WaveComponent buildGroup(WaveGroupDefinition group) {
        List<WaveComponent> spawns = new ArrayList<>();
        for (int index = 0; index < group.count(); index++) {
            int tick = group.delayTicks() + index * group.intervalTicks();
            spawns.add(new SpawnEntry(group.enemyType(), tick));
        }
        return new WaveGroup(spawns);
    }
}
