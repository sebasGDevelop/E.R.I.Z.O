package dev.erizo.firewall.defense;

import dev.erizo.firewall.shared.GridPosition;
import java.util.List;

/** The ordered walk from SPAWN to NODE, measured as a distance in tiles. */
public final class EnemyPath {

    private final List<GridPosition> steps;

    private EnemyPath(List<GridPosition> steps) {
        this.steps = steps;
    }

    /** Builds a path over a map, rejecting one that is too short or does not run SPAWN to NODE. */
    public static EnemyPath over(MapLayout map, List<GridPosition> steps) {
        if (steps == null || steps.size() < 2) {
            throw new InvalidMapException("Path must have at least a spawn and a node");
        }
        if (map.tileAt(steps.get(0)) != TileType.SPAWN) {
            throw new InvalidMapException("Path must start at a SPAWN tile");
        }
        if (map.tileAt(steps.get(steps.size() - 1)) != TileType.NODE) {
            throw new InvalidMapException("Path must end at a NODE tile");
        }
        return new EnemyPath(List.copyOf(steps));
    }

    /** The walkable length in tiles from spawn to node. */
    public double length() {
        return steps.size() - 1;
    }

    /** The grid cell reached after walking the given distance, clamped to the path ends. */
    public GridPosition positionAt(double distance) {
        if (distance <= 0) {
            return steps.get(0);
        }
        if (distance >= length()) {
            return steps.get(steps.size() - 1);
        }
        return steps.get((int) Math.floor(distance));
    }

    /** Tells whether the given distance has reached the node. */
    public boolean reachesNode(double distance) {
        return distance >= length();
    }

    public GridPosition spawn() {
        return steps.get(0);
    }

    public GridPosition node() {
        return steps.get(steps.size() - 1);
    }
}
