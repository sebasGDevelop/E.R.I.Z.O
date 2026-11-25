package dev.erizo.firewall.defense;

import dev.erizo.firewall.shared.EnemyId;

/** Creates enemies from content type ids, assigning each a match-unique id. */
public final class EnemyFactory {

    private long nextId;

    public EnemyFactory() {
        this.nextId = 1;
    }

    /** Creates an enemy for the given content type id, rejecting an unknown type. */
    public Enemy create(String enemyType) {
        EnemyKind kind = EnemyKind.fromContentId(enemyType);
        return Enemy.spawn(new EnemyId(nextId++), kind);
    }
}
