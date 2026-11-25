package dev.erizo.firewall.defense;

import dev.erizo.firewall.shared.GridPosition;
import dev.erizo.firewall.shared.TowerId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** The running match: map, path, economy, node integrity and placed towers. */
public final class DefenseMatch {

    private final MapLayout map;
    private final EnemyPath path;
    private final List<Tower> towers;
    private Bits bits;
    private NodeIntegrity nodeIntegrity;
    private long nextTowerId;

    private DefenseMatch(MapLayout map, EnemyPath path) {
        this.map = map;
        this.path = path;
        this.towers = new ArrayList<>();
        this.bits = Bits.starting();
        this.nodeIntegrity = NodeIntegrity.full();
        this.nextTowerId = 1;
    }

    /** Starts a match on the given map and path with full node integrity and starting bits. */
    public static DefenseMatch start(MapLayout map, EnemyPath path) {
        return new DefenseMatch(map, path);
    }

    /** Places a tower of the given kind, rejecting an illegal tile or insufficient bits. */
    public Tower placeTower(TowerKind kind, GridPosition position) {
        requireBuildableTile(position);
        requireFreeTile(position);
        bits = bits.spend(kind.cost());
        Tower tower = new Tower(new TowerId(nextTowerId++), kind, position);
        towers.add(tower);
        return tower;
    }

    /** Adds a disinfected enemy's bounty to the player's bits. */
    public void rewardDisinfect(Enemy enemy) {
        bits = bits.add(enemy.bounty());
    }

    public Optional<Tower> towerAt(GridPosition position) {
        return towers.stream().filter(tower -> tower.position().equals(position)).findFirst();
    }

    public List<Tower> towers() {
        return List.copyOf(towers);
    }

    public Bits bits() {
        return bits;
    }

    public NodeIntegrity nodeIntegrity() {
        return nodeIntegrity;
    }

    private void requireBuildableTile(GridPosition position) {
        if (map.tileAt(position) != TileType.BUILD) {
            throw new InvalidPlacementException("Tile " + position + " is not a BUILD tile");
        }
    }

    private void requireFreeTile(GridPosition position) {
        if (towerAt(position).isPresent()) {
            throw new InvalidPlacementException("Tile " + position + " is already occupied");
        }
    }
}
