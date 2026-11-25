package dev.erizo.firewall.prototype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolves an enemy type name to its stats behavior and optional event ability.
 */
final class EnemyCatalog {

    private final Map<String, EnemyBehavior> behaviors = new HashMap<>();
    private final Map<String, SpawnsOnEvent> abilities = new HashMap<>();

    EnemyCatalog(List<EnemyBehavior> entries, Map<String, SpawnsOnEvent> abilities) {
        for (EnemyBehavior behavior : entries) {
            behaviors.put(behavior.type(), behavior);
        }
        this.abilities.putAll(abilities);
    }

    EnemyBehavior behaviorFor(String type) {
        EnemyBehavior behavior = behaviors.get(type);
        if (behavior == null) {
            throw new IllegalArgumentException("unknown enemy type " + type);
        }
        return behavior;
    }

    SpawnsOnEvent abilityFor(String type) {
        return abilities.get(type);
    }
}
