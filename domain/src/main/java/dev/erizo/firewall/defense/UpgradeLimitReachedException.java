package dev.erizo.firewall.defense;

/** Thrown when a tower already carries the maximum number of upgrades. */
public class UpgradeLimitReachedException extends RuntimeException {

    public UpgradeLimitReachedException(String message) {
        super(message);
    }
}
