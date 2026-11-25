package dev.erizo.firewall.defense;

import dev.erizo.firewall.shared.EnemyId;
import dev.erizo.firewall.shared.GridPosition;

/** A single malware unit walking the path, with health and a behavioural state. */
public final class Enemy {

    private final EnemyId id;
    private final EnemyKind kind;
    private int health;
    private double progress;
    private EnemyState state;

    private Enemy(EnemyId id, EnemyKind kind, int health, EnemyState state) {
        this.id = id;
        this.kind = kind;
        this.health = health;
        this.progress = 0.0;
        this.state = state;
    }

    /** Creates an enemy of the given kind at the start of the path with full health. */
    public static Enemy spawn(EnemyId id, EnemyKind kind) {
        EnemyState initial = kind.startsDisguised() ? new DisguisedState() : new NormalState();
        return new Enemy(id, kind, kind.stats().health(), initial);
    }

    /** Advances one tick: moves along the path if still moving, then ages the state. */
    public void advance() {
        if (state.isMoving()) {
            progress += kind.stats().speedTilesPerTick() * state.speedMultiplier();
        }
        state = state.afterTick();
    }

    /** Applies damage and disinfects the enemy when its health is spent. */
    public void takeDamage(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Damage cannot be negative");
        }
        health = Math.max(0, health - amount);
        if (health == 0) {
            state = new DisinfectedState();
        }
    }

    /** Reveals a disguised enemy so towers can target it. */
    public void reveal() {
        if (!state.canBeTargeted() && !state.isDisinfected()) {
            state = new NormalState();
        }
    }

    /** Slows the enemy for a number of ticks unless it has left play. */
    public void slow(double multiplier, int ticks) {
        if (!state.isDisinfected()) {
            state = new SlowedState(multiplier, ticks);
        }
    }

    public GridPosition positionOn(EnemyPath path) {
        return path.positionAt(progress);
    }

    public boolean hasReachedNode(EnemyPath path) {
        return path.reachesNode(progress);
    }

    public boolean canBeTargeted() {
        return state.canBeTargeted();
    }

    public boolean isDisinfected() {
        return state.isDisinfected();
    }

    public boolean isSwarmMember() {
        return kind.swarmMember();
    }

    public EnemyId id() {
        return id;
    }

    public EnemyKind kind() {
        return kind;
    }

    public int health() {
        return health;
    }

    public double progress() {
        return progress;
    }

    public int leak() {
        return kind.stats().nodeDamage();
    }

    public int bounty() {
        return kind.stats().bitsReward();
    }
}
