package dev.erizo.firewall.content;

import java.util.List;

/** The data for one spy mission: its id, type, time limit and configuration items. */
public record MissionDefinition(String id, String type, int timeLimitTicks, List<String> items) {

    public MissionDefinition {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Mission id is required");
        }
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Mission type is required");
        }
        if (timeLimitTicks <= 0) {
            throw new IllegalArgumentException("Time limit must be positive");
        }
        items = items == null ? List.of() : List.copyOf(items);
    }
}
