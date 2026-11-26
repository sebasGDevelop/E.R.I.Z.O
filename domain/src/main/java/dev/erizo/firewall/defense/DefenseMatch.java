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
    private final List<Enemy> enemies;
    private Bits bits;
    private NodeIntegrity nodeIntegrity;
    private long nextTowerId;

    private boolean lost;

    private DefenseMatch(MapLayout map, EnemyPath path) {
        this.map = map;
        this.path = path;
        this.towers = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.bits = Bits.starting();
        this.nodeIntegrity = NodeIntegrity.full();
        this.nextTowerId = 1;
        this.lost = false;
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

    /** Adds an enemy to the field, as a wave spawns or a test sets up. */
    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    /** Changes a placed tower's targeting strategy. */
    public void changeTarget(Tower tower, TargetStrategy strategy) {
        tower.retargetWith(strategy);
    }

    /** Upgrades a tower, rejecting it when the limit is reached or bits are short. */
    public void upgradeTower(Tower tower, UpgradeKind upgrade) {
        tower.requireUpgradeAllowed();
        bits = bits.spend(upgrade.cost());
        tower.upgradeWith(upgrade);
    }

    /** Sells a tower, removing it and refunding 70% of the bits spent on it. */
    public void sellTower(Tower tower) {
        if (!towers.remove(tower)) {
            throw new InvalidPlacementException("Tower is not on the field");
        }
        bits = bits.add(tower.sellRefund());
    }

    /** Upgrades the tower on the given tile, rejecting an empty tile. */
    public void upgradeTowerAt(GridPosition position, UpgradeKind upgrade) {
        upgradeTower(requireTowerAt(position), upgrade);
    }

    /** Sells the tower on the given tile, rejecting an empty tile. */
    public void sellTowerAt(GridPosition position) {
        sellTower(requireTowerAt(position));
    }

    /** Retargets the tower on the given tile, rejecting an empty tile. */
    public void changeTargetAt(GridPosition position, TargetStrategyKind strategyKind) {
        requireTowerAt(position).retargetWith(strategyKind.create());
    }

    /** Runs every tower's per-tick action against the enemies within its range. */
    public void tickTowers() {
        for (Tower tower : towers) {
            tower.act(enemiesInRangeOf(tower), path);
        }
    }

    /** Moves every active enemy one tick, banking bounties and leaks at the node. */
    public void advanceEnemies() {
        for (Enemy enemy : enemies) {
            advanceEnemy(enemy);
        }
    }

    /** Adds a disinfected enemy's bounty to the player's bits. */
    public void rewardDisinfect(Enemy enemy) {
        bits = bits.add(enemy.bounty());
    }

    public boolean isLost() {
        return lost;
    }

    public List<Enemy> enemies() {
        return List.copyOf(enemies);
    }

    public List<Enemy> activeEnemies() {
        return enemies.stream().filter(enemy -> !enemy.isResolved()).toList();
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

    private List<Enemy> enemiesInRangeOf(Tower tower) {
        return enemies.stream()
                .filter(enemy -> !enemy.isResolved())
                .filter(enemy -> tower.covers(enemy, path))
                .toList();
    }

    private void advanceEnemy(Enemy enemy) {
        if (enemy.isResolved()) {
            return;
        }
        if (enemy.isDisinfected()) {
            rewardDisinfect(enemy);
            enemy.markResolved();
            return;
        }
        enemy.advance();
        if (enemy.hasReachedNode(path)) {
            leakAt(enemy);
        }
    }

    private void leakAt(Enemy enemy) {
        nodeIntegrity = nodeIntegrity.subtract(enemy.leak());
        enemy.markResolved();
        if (nodeIntegrity.isBreached()) {
            lost = true;
        }
    }

    private Tower requireTowerAt(GridPosition position) {
        return towerAt(position)
                .orElseThrow(() -> new InvalidPlacementException("No tower on tile " + position));
    }
}
