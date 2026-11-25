package dev.erizo.firewall.prototype.adapter;

import dev.erizo.firewall.prototype.GridPosition;
import dev.erizo.firewall.prototype.MapData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads the console map and its path from a classpath resource.
 */
public final class ResourceMapLoader {

    private final String resource;

    public ResourceMapLoader(String resource) {
        this.resource = resource;
    }

    public MapData load() {
        List<String> lines = readLines();
        int height = lines.size();
        int width = lines.get(0).length();
        char[][] grid = new char[height][width];
        GridPosition spawn = null;
        GridPosition node = null;
        for (int y = 0; y < height; y++) {
            char[] row = lines.get(y).toCharArray();
            for (int x = 0; x < width; x++) {
                grid[y][x] = row[x];
                if (row[x] == 'S') {
                    spawn = new GridPosition(x, y);
                } else if (row[x] == 'N') {
                    node = new GridPosition(x, y);
                }
            }
        }
        return new MapData(grid, buildPath(spawn, node));
    }

    private List<GridPosition> buildPath(GridPosition spawn, GridPosition node) {
        List<GridPosition> path = new ArrayList<>();
        int y = spawn.y();
        for (int x = spawn.x(); x <= node.x(); x++) {
            path.add(new GridPosition(x, y));
        }
        return path;
    }

    private List<String> readLines() {
        try (InputStream in = ResourceMapLoader.class.getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("missing resource " + resource);
            }
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
