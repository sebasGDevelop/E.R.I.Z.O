package dev.erizo.firewall.defense;

/** A slowed enemy moves at a reduced speed for a limited number of ticks. */
public final class SlowedState implements EnemyState {

    private final double multiplier;
    private final int remainingTicks;

    public SlowedState(double multiplier, int remainingTicks) {
        if (multiplier <= 0 || multiplier >= 1) {
            throw new IllegalArgumentException("Slow multiplier must be between 0 and 1");
        }
        if (remainingTicks <= 0) {
            throw new IllegalArgumentException("Slow duration must be positive");
        }
        this.multiplier = multiplier;
        this.remainingTicks = remainingTicks;
    }

    @Override
    public double speedMultiplier() {
        return multiplier;
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
        if (remainingTicks <= 1) {
            return new NormalState();
        }
        return new SlowedState(multiplier, remainingTicks - 1);
    }
}
