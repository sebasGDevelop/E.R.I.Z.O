package dev.erizo.firewall.prototype;

/**
 * The tower's view of an enemy it can aim at and damage.
 */
interface Targetable {

    int id();

    double progress();

    int bounty();

    boolean canBeTargeted();

    double applyDamage(int amount);

    void disinfect();
}
