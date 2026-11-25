package dev.erizo.firewall.prototype;

import java.util.List;

/**
 * The console map grid and the ordered path enemies walk.
 */
public record MapData(char[][] grid, List<GridPosition> path) {
}
