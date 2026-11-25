package dev.erizo.firewall.prototype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolves an enemy type name to the behavior that governs it.
 */
final class EnemyCatalog {

    private final Map<String, EnemyBehavior> behaviors = new HashMap<>();

    EnemyCatalog(List<EnemyBehavior> entries) {
        for (EnemyBehavior behavior : entries) {
            behaviors.put(behavior.type(), behavior);
        }
    }

    EnemyBehavior behaviorFor(String type) {
        EnemyBehavior behavior = behaviors.get(type);
        if (behavior == null) {
            throw new IllegalArgumentException("unknown enemy type " + type);
        }
        return behavior;
    }
}
