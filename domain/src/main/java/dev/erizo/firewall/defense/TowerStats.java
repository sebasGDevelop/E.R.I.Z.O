package dev.erizo.firewall.defense;

/** The fixed figures for a tower kind. */
public record TowerStats(int damage, double rangeTiles, int fireRateTicks, int cost, int sellRefund) {

    public TowerStats {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage cannot be negative");
        }
        if (rangeTiles <= 0) {
            throw new IllegalArgumentException("Range must be positive");
        }
        if (fireRateTicks < 0) {
            throw new IllegalArgumentException("Fire rate cannot be negative");
        }
        if (cost < 0) {
            throw new IllegalArgumentException("Cost cannot be negative");
        }
        if (sellRefund < 0) {
            throw new IllegalArgumentException("Sell refund cannot be negative");
        }
    }
}
