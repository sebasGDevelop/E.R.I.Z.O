package dev.erizo.firewall.defense;

/** The behavioural condition of an enemy, controlling movement and targeting. */
public interface EnemyState {

    /** The factor applied to the enemy's base speed while in this state. */
    double speedMultiplier();

    /** Whether towers may select this enemy as a target. */
    boolean canBeTargeted();

    /** Whether the enemy still advances along the path. */
    boolean isMoving();

    /** Whether the enemy has been disinfected and left play. */
    boolean isDisinfected();

    /** The state after one tick elapses, which may be this same state. */
    EnemyState afterTick();
}
