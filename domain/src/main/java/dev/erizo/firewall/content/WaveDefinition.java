package dev.erizo.firewall.content;

import java.util.List;

/** The data for one wave: its number and the groups it releases. */
public record WaveDefinition(int number, List<WaveGroupDefinition> groups) {

    public WaveDefinition {
        if (number < 0) {
            throw new IllegalArgumentException("Wave number cannot be negative");
        }
        if (groups == null || groups.isEmpty()) {
            throw new IllegalArgumentException("A wave needs at least one group");
        }
        groups = List.copyOf(groups);
    }
}
