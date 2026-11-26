package dev.erizo.firewall.client.text;

import dev.erizo.firewall.defense.MapLayout;
import dev.erizo.firewall.defense.TileType;
import dev.erizo.firewall.shared.GridPosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Turns a map into rows of text, one glyph per tile, for the debug view. */
public final class MapTextRenderer {

    private static final Map<TileType, Character> GLYPHS = Map.of(
            TileType.PATH, '.',
            TileType.BUILD, '#',
            TileType.BLOCKED, 'X',
            TileType.SPAWN, 'S',
            TileType.NODE, 'N');

    /** Renders the map top row first, each row a string of tile glyphs. */
    public List<String> render(MapLayout map) {
        List<String> rows = new ArrayList<>();
        for (int y = 0; y < map.height(); y++) {
            rows.add(renderRow(map, y));
        }
        return rows;
    }

    private String renderRow(MapLayout map, int y) {
        StringBuilder row = new StringBuilder();
        for (int x = 0; x < map.width(); x++) {
            row.append(GLYPHS.get(map.tileAt(new GridPosition(x, y))));
        }
        return row.toString();
    }
}
