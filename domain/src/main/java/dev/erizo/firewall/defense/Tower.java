package dev.erizo.firewall.defense;

import dev.erizo.firewall.shared.GridPosition;
import dev.erizo.firewall.shared.TowerId;
import java.util.List;

/** A placed defensive tower occupying one BUILD tile. */
public final class Tower {

    private final TowerId id;
    private final TowerKind kind;
    private final GridPosition position;
    private TargetStrategy targetStrategy;
    private int cooldownRemaining;

    public Tower(TowerId id, TowerKind kind, GridPosition position) {
        this.id = id;
        this.kind = kind;
        this.position = position;
        this.targetStrategy = new FirstTarget();
        this.cooldownRemaining = 0;
    }

    /** Performs this tower's per-tick action on the enemies within its range. */
    public void act(List<Enemy> enemiesInRange, EnemyPath path) {
        kind.behavior().act(this, enemiesInRange, path);
    }

    /** Whether the tower may fire this tick. */
    public boolean isReady() {
        return cooldownRemaining <= 0;
    }

    /** Counts down one tick toward being ready to fire again. */
    public void tickCooldown() {
        if (cooldownRemaining > 0) {
            cooldownRemaining--;
        }
    }

    /** Starts the cooldown again after firing. */
    public void startCooldown() {
        cooldownRemaining = kind.stats().fireRateTicks();
    }

    /** Replaces the tower's targeting strategy. */
    public void retargetWith(TargetStrategy strategy) {
        this.targetStrategy = strategy;
    }

    public TargetStrategy targetStrategy() {
        return targetStrategy;
    }

    public boolean covers(Enemy enemy, EnemyPath path) {
        return position.distanceTo(enemy.positionOn(path)) <= kind.stats().rangeTiles();
    }

    public TowerId id() {
        return id;
    }

    public TowerKind kind() {
        return kind;
    }

    public GridPosition position() {
        return position;
    }

    public int cost() {
        return kind.cost();
    }

    public int damage() {
        return kind.stats().damage();
    }
}
