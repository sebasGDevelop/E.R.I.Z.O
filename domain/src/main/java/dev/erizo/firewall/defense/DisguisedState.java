package dev.erizo.firewall.defense;

/** A disguised enemy moves normally but cannot be targeted until revealed. */
public final class DisguisedState implements EnemyState {

    @Override
    public double speedMultiplier() {
        return 1.0;
    }

    @Override
    public boolean canBeTargeted() {
        return false;
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
