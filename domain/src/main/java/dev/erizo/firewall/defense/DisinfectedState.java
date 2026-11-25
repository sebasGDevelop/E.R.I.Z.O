package dev.erizo.firewall.defense;

/** A disinfected enemy has left play: it no longer moves and cannot be targeted. */
public final class DisinfectedState implements EnemyState {

    @Override
    public double speedMultiplier() {
        return 0.0;
    }

    @Override
    public boolean canBeTargeted() {
        return false;
    }

    @Override
    public boolean isMoving() {
        return false;
    }

    @Override
    public boolean isDisinfected() {
        return true;
    }

    @Override
    public EnemyState afterTick() {
        return this;
    }
}
