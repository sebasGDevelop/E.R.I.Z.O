package dev.erizo.firewall.shared;

/** A cell coordinate on the map grid. */
public record GridPosition(int x, int y) {

    /** The straight-line distance in tiles to another cell. */
    public double distanceTo(GridPosition other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
