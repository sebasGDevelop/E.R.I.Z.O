package dev.erizo.firewall.defense;

/** The effective range and shot count of a tower after its upgrades. */
public interface TowerProfile {

    /** The firing range in tiles. */
    double rangeTiles();

    /** How many times the tower fires each time its cooldown elapses. */
    int shotsPerCooldown();
}
