package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.erizo.firewall.shared.GridPosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class MapLayoutTest {

    private static List<List<TileType>> buildRows() {
        List<List<TileType>> rows = new ArrayList<>();
        for (int y = 0; y < MapLayout.HEIGHT; y++) {
            List<TileType> row = new ArrayList<>(Collections.nCopies(MapLayout.WIDTH, TileType.BUILD));
            rows.add(row);
        }
        return rows;
    }

    @Test
    void tileAt_whenCellWasSet_returnsThatTile() {
        List<List<TileType>> rows = buildRows();
        rows.get(3).set(5, TileType.PATH);

        MapLayout map = MapLayout.fromRows(rows);

        assertThat(map.tileAt(new GridPosition(5, 3))).isEqualTo(TileType.PATH);
    }

    @Test
    void fromRows_whenRowCountWrong_throws() {
        List<List<TileType>> rows = buildRows();
        rows.remove(0);

        assertThatThrownBy(() -> MapLayout.fromRows(rows))
                .isInstanceOf(InvalidMapException.class);
    }

    @Test
    void fromRows_whenRowWidthWrong_throws() {
        List<List<TileType>> rows = buildRows();
        rows.get(2).remove(0);

        assertThatThrownBy(() -> MapLayout.fromRows(rows))
                .isInstanceOf(InvalidMapException.class);
    }

    @Test
    void tileAt_whenPositionOutsideGrid_throws() {
        MapLayout map = MapLayout.fromRows(buildRows());

        assertThatThrownBy(() -> map.tileAt(new GridPosition(MapLayout.WIDTH, 0)))
                .isInstanceOf(InvalidMapException.class);
    }

    @Test
    void dimensions_whenBuilt_areTwelveByEight() {
        MapLayout map = MapLayout.fromRows(buildRows());

        assertThat(List.of(map.width(), map.height())).containsExactly(12, 8);
    }

    @Test
    void fromRows_whenCellMissing_throws() {
        List<List<TileType>> rows = buildRows();
        rows.get(1).set(4, null);

        assertThatThrownBy(() -> MapLayout.fromRows(rows))
                .isInstanceOf(InvalidMapException.class);
    }

    @Test
    void contains_whenPositionInsideGrid_isTrue() {
        MapLayout map = MapLayout.fromRows(buildRows());

        assertThat(map.contains(new GridPosition(0, 0))).isTrue();
    }
}
