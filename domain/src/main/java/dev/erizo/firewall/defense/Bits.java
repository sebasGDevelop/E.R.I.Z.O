package dev.erizo.firewall.defense;

/** The player's spendable currency during a match. */
public record Bits(int amount) {

    public static final int STARTING_AMOUNT = 100;

    public Bits {
        if (amount < 0) {
            throw new IllegalArgumentException("Bits cannot be negative");
        }
    }

    /** The bits a player starts a match with. */
    public static Bits starting() {
        return new Bits(STARTING_AMOUNT);
    }

    /** Returns the balance after spending, rejecting a cost that exceeds it. */
    public Bits spend(int cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("Cost cannot be negative");
        }
        if (cost > amount) {
            throw new NotEnoughBitsException("Need " + cost + " bits but only " + amount + " available");
        }
        return new Bits(amount - cost);
    }

    /** Returns the balance after earning the given amount. */
    public Bits add(int earned) {
        if (earned < 0) {
            throw new IllegalArgumentException("Earned bits cannot be negative");
        }
        return new Bits(amount + earned);
    }

    /** Tells whether the balance can cover the given cost. */
    public boolean canAfford(int cost) {
        return cost <= amount;
    }
}
