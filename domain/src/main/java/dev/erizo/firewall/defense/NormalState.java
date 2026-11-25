package dev.erizo.firewall.defense;

/** The default state: the enemy moves at full speed and can be targeted. */
public final class NormalState implements EnemyState {

    @Override
    public double speedMultiplier() {
        return 1.0;
    }

    @Override
    public boolean canBeTargeted() {
        return true;
    }

    @Override
    public boolean isMoving() {
        return true;
    }

    @Override
    public boolean isDisinfected() {
        return false;
    }

    @Override
    public EnemyState afterTick() {
        return this;
    }
}
