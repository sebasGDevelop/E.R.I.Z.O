package dev.erizo.firewall.prototype;

/**
 * Mutable state of one enemy walking the path during the prototype wave.
 */
final class Enemy implements PathWalker, Targetable {
    int id;
    String type;
    SpawnsOnEvent ability;
    double integrity;
    double speed;
    int leak;
    int bounty;
    double progress;
    int ageTicks;
    boolean duplicated;
    boolean released;
    boolean disinfected;
    boolean leaked;

    @Override
    public int id() {
        return id;
    }

    @Override
    public double progress() {
        return progress;
    }

    @Override
    public double speed() {
        return speed;
    }

    @Override
    public boolean isActive() {
        return !disinfected && !leaked;
    }

    @Override
    public void advance(double tiles) {
        progress += tiles;
    }

    @Override
    public int bounty() {
        return bounty;
    }

    @Override
    public boolean canBeTargeted() {
        return isActive();
    }

    @Override
    public double applyDamage(int amount) {
        integrity -= amount;
        return integrity;
    }

    @Override
    public void disinfect() {
        disinfected = true;
    }
}
