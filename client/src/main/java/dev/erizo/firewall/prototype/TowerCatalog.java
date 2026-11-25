package dev.erizo.firewall.prototype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolves a tower type name to the behavior that governs it.
 */
final class TowerCatalog {

    private final Map<String, TowerBehavior> behaviors = new HashMap<>();

    TowerCatalog(List<TowerBehavior> entries) {
        for (TowerBehavior behavior : entries) {
            behaviors.put(behavior.type(), behavior);
        }
    }

    TowerBehavior behaviorFor(String type) {
        TowerBehavior behavior = behaviors.get(type);
        if (behavior == null) {
            throw new IllegalArgumentException("unknown tower type " + type);
        }
        return behavior;
    }
}
