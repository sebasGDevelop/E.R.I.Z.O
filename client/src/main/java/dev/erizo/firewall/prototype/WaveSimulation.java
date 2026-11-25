package dev.erizo.firewall.prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * Advances one defense wave in fixed ticks and delegates type-specific rules.
 */
final class WaveSimulation implements WaveState, TowerContext {

    private static final int NODE_START_INTEGRITY = 20;
    private static final int STARTING_BITS = 100;

    private final SimulationOutput output;
    private final RandomSource random;
    private final MatchStorage storage;
    private final ConsoleMapRenderer renderer;
    private final EnemyCatalog enemyCatalog;
    private final TowerCatalog towerCatalog;

    private final char[][] map;
    private final List<GridPosition> path;
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Tower> towers = new ArrayList<>();
    private final List<Enemy> pendingSpawns = new ArrayList<>();

    private int tick;
    private int bits = STARTING_BITS;
    private int nodeIntegrity = NODE_START_INTEGRITY;
    private int nextEnemyId;
    private final List<SpawnOrder> spawnSchedule = new ArrayList<>();

    private static final class SpawnOrder {
        int tick;
        String type;
    }

    WaveSimulation(SimulationOutput output, RandomSource random, MatchStorage storage,
            ConsoleMapRenderer renderer, EnemyCatalog enemyCatalog, TowerCatalog towerCatalog,
            MapData mapData, WaveDefinition wave) {
        this.output = output;
        this.random = random;
        this.storage = storage;
        this.renderer = renderer;
        this.enemyCatalog = enemyCatalog;
        this.towerCatalog = towerCatalog;
        this.map = mapData.grid();
        this.path = mapData.path();
        scheduleSpawns(wave);
    }

    void run(List<String> commands) {
        for (String command : commands) {
            handleCommand(command);
        }
        storage.save(new MatchSnapshot(tick, bits, nodeIntegrity));
        output.line("SAVED tick=" + tick + " bits=" + bits + " integrity=" + nodeIntegrity);
    }

    private void handleCommand(String command) {
        String[] parts = command.trim().split("\\s+");
        if (parts[0].equals("PLACE")) {
            placeTower(parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        } else if (parts[0].equals("RUN")) {
            int ticks = Integer.parseInt(parts[1]);
            for (int i = 0; i < ticks; i++) {
                advanceTick();
            }
        }
    }

    private void placeTower(String kind, int x, int y) {
        TowerBehavior behavior = towerCatalog.behaviorFor(kind);
        if (bits < behavior.cost()) {
            output.line("REJECTED place " + kind + ": not enough bits");
            return;
        }
        Tower tower = new Tower();
        tower.type = kind;
        tower.behavior = behavior;
        tower.x = x;
        tower.y = y;
        tower.cooldownLeft = 0;
        towers.add(tower);
        bits -= behavior.cost();
        output.line("PLACED " + kind + " at " + x + "," + y + " bits=" + bits);
    }

    private void advanceTick() {
        tick++;
        spawnDueEnemies();
        moveEnemies();
        applyEnemyAbilities();
        updateTowers();
        removeFinishedEnemies();
        if (tick % 10 == 0) {
            renderer.render(map, path, towers, enemies, tick, bits, nodeIntegrity);
        }
    }

    private void spawnDueEnemies() {
        for (SpawnOrder order : spawnSchedule) {
            if (order.tick == tick) {
                spawnEnemy(order.type);
            }
        }
    }

    private void spawnEnemy(String type) {
        Enemy enemy = createEnemy(type);
        enemy.progress = random.nextInt(1);
        enemies.add(enemy);
        output.line("SPAWN " + type + " id=" + enemy.id + " tick=" + tick);
    }

    private Enemy createEnemy(String type) {
        EnemyBehavior behavior = enemyCatalog.behaviorFor(type);
        Enemy enemy = new Enemy();
        enemy.id = nextEnemyId++;
        enemy.type = type;
        enemy.ability = enemyCatalog.abilityFor(type);
        behavior.configure(enemy);
        return enemy;
    }

    private void moveEnemies() {
        for (Enemy enemy : enemies) {
            PathWalker walker = enemy;
            if (!walker.isActive()) {
                continue;
            }
            enemy.ageTicks++;
            walker.advance(walker.speed() / 10.0);
            if (walker.progress() >= path.size() - 1) {
                enemy.leaked = true;
                nodeIntegrity -= enemy.leak;
                output.line("LEAK " + enemy.type + " id=" + enemy.id
                        + " integrity=" + nodeIntegrity);
            }
        }
    }

    private void applyEnemyAbilities() {
        for (Enemy enemy : enemies) {
            if (!enemy.isActive() || enemy.ability == null) {
                continue;
            }
            enemy.ability.applyAbility(enemy, this);
        }
        enemies.addAll(pendingSpawns);
        pendingSpawns.clear();
    }

    private void updateTowers() {
        List<Targetable> targets = new ArrayList<>(enemies);
        for (Tower tower : towers) {
            tower.behavior.update(tower, targets, path, this);
        }
    }

    private void removeFinishedEnemies() {
        enemies.removeIf(enemy -> enemy.disinfected || enemy.leaked);
    }

    @Override
    public int pathLength() {
        return path.size();
    }

    @Override
    public void spawnOffspring(String type, double progress) {
        Enemy enemy = createEnemy(type);
        enemy.progress = progress;
        pendingSpawns.add(enemy);
        output.line("SPAWN " + type + " id=" + enemy.id + " tick=" + tick);
    }

    @Override
    public int awardBounty(int bounty) {
        bits += bounty;
        return bits;
    }

    private void scheduleSpawns(WaveDefinition wave) {
        for (WaveEntry entry : wave.entries()) {
            for (int i = 0; i < entry.count(); i++) {
                SpawnOrder order = new SpawnOrder();
                order.tick = entry.delayTicks() + i * entry.intervalTicks() + 1;
                order.type = entry.enemyType();
                spawnSchedule.add(order);
            }
        }
    }
}
