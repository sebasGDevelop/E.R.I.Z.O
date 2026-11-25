package dev.erizo.firewall.defense;

import dev.erizo.firewall.shared.GridPosition;
import java.util.List;

/** A fixed 12x8 grid of tiles that enemies walk and towers are built on. */
public final class MapLayout {

    public static final int WIDTH = 12;
    public static final int HEIGHT = 8;

    private final TileType[][] tiles;

    private MapLayout(TileType[][] tiles) {
        this.tiles = tiles;
    }

    /** Builds a map from rows of tiles, top row first, rejecting a wrong size or null cell. */
    public static MapLayout fromRows(List<List<TileType>> rows) {
        if (rows == null || rows.size() != HEIGHT) {
            throw new InvalidMapException("Map must have exactly " + HEIGHT + " rows");
        }
        TileType[][] grid = new TileType[HEIGHT][WIDTH];
        for (int y = 0; y < HEIGHT; y++) {
            grid[y] = readRow(rows.get(y), y);
        }
        return new MapLayout(grid);
    }

    private static TileType[] readRow(List<TileType> row, int y) {
        if (row == null || row.size() != WIDTH) {
            throw new InvalidMapException("Row " + y + " must have exactly " + WIDTH + " tiles");
        }
        TileType[] cells = new TileType[WIDTH];
        for (int x = 0; x < WIDTH; x++) {
            cells[x] = requireTile(row.get(x), x, y);
        }
        return cells;
    }

    private static TileType requireTile(TileType tile, int x, int y) {
        if (tile == null) {
            throw new InvalidMapException("Tile at (" + x + ", " + y + ") is missing");
        }
        return tile;
    }

    /** Returns the tile at the given position, rejecting an out-of-bounds position. */
    public TileType tileAt(GridPosition position) {
        if (!contains(position)) {
            throw new InvalidMapException("Position " + position + " is outside the map");
        }
        return tiles[position.y()][position.x()];
    }

    /** Tells whether the position lies inside the grid. */
    public boolean contains(GridPosition position) {
        return position.x() >= 0
                && position.x() < WIDTH
                && position.y() >= 0
                && position.y() < HEIGHT;
    }

    public int width() {
        return WIDTH;
    }

    public int height() {
        return HEIGHT;
    }
}
