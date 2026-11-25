package dev.erizo.firewall.defense;

/** The Central Node's remaining integrity, lost when enemies leak through. */
public record NodeIntegrity(int value) {

    public static final int STARTING_VALUE = 20;

    public NodeIntegrity {
        if (value < 0) {
            throw new IllegalArgumentException("Node integrity cannot be negative");
        }
    }

    /** The full node integrity at the start of a match. */
    public static NodeIntegrity full() {
        return new NodeIntegrity(STARTING_VALUE);
    }

    /** Returns the integrity after an enemy leaks the given amount, floored at zero. */
    public NodeIntegrity subtract(int leak) {
        if (leak < 0) {
            throw new IllegalArgumentException("Leak cannot be negative");
        }
        return new NodeIntegrity(Math.max(0, value - leak));
    }

    /** Tells whether the node has fallen, ending the match as lost. */
    public boolean isBreached() {
        return value == 0;
    }
}
