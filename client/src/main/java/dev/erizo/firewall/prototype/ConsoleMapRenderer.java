package dev.erizo.firewall.prototype;

import java.util.List;

/**
 * Renders a snapshot of the map with towers and enemies to the output.
 */
final class ConsoleMapRenderer {

    private final SimulationOutput output;

    ConsoleMapRenderer(SimulationOutput output) {
        this.output = output;
    }

    void render(char[][] map, List<GridPosition> path, List<Tower> towers,
            List<Enemy> enemies, int tick, int bits, int nodeIntegrity) {
        int height = map.length;
        int width = map[0].length;
        char[][] frame = new char[height][width];
        for (int y = 0; y < height; y++) {
            System.arraycopy(map[y], 0, frame[y], 0, width);
        }
        for (Tower tower : towers) {
            frame[tower.y][tower.x] = 'T';
        }
        for (Enemy enemy : enemies) {
            GridPosition tile = path.get((int) Math.floor(enemy.progress));
            frame[tile.y()][tile.x()] = 'e';
        }
        output.line("--- tick " + tick + " bits=" + bits
                + " integrity=" + nodeIntegrity + " ---");
        for (int y = 0; y < height; y++) {
            output.line(new String(frame[y]));
        }
    }
}
