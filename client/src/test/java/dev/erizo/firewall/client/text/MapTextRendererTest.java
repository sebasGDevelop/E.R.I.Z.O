package dev.erizo.firewall.client.text;

import static org.assertj.core.api.Assertions.assertThat;

import dev.erizo.firewall.defense.MapLayout;
import dev.erizo.firewall.defense.TileType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class MapTextRendererTest {

    private final MapTextRenderer renderer = new MapTextRenderer();

    private static MapLayout mapWithSpawnAndNode() {
        List<List<TileType>> rows = new ArrayList<>();
        for (int y = 0; y < MapLayout.HEIGHT; y++) {
            rows.add(new ArrayList<>(Collections.nCopies(MapLayout.WIDTH, TileType.BUILD)));
        }
        rows.get(0).set(0, TileType.SPAWN);
        rows.get(0).set(1, TileType.NODE);
        return MapLayout.fromRows(rows);
    }

    @Test
    void render_producesOneRowPerMapRow() {
        List<String> rows = renderer.render(mapWithSpawnAndNode());

        assertThat(rows).hasSize(MapLayout.HEIGHT);
    }

    @Test
    void render_rowIsAsWideAsTheMap() {
        List<String> rows = renderer.render(mapWithSpawnAndNode());

        assertThat(rows.get(0)).hasSize(MapLayout.WIDTH);
    }

    @Test
    void render_drawsSpawnAndNodeGlyphs() {
        List<String> rows = renderer.render(mapWithSpawnAndNode());

        assertThat(rows.get(0)).startsWith("SN");
    }
}
