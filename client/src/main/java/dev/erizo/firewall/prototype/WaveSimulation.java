package dev.erizo.firewall.prototype;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Advances one defense wave in fixed ticks and delegates type-specific rules.
 */
final class WaveSimulation implements WaveState, TowerContext {

    private static final int WIDTH = 12;
    private static final int HEIGHT = 8;
    private static final int NODE_START_INTEGRITY = 20;
    private static final int STARTING_BITS = 100;

    private final PrintStream out;
    private final ConsoleMapRenderer renderer;
    private final MatchSaver saver;
    private final EnemyCatalog enemyCatalog;
    private final TowerCatalog towerCatalog;
    private final Random random = new Random(42);

    private char[][] map;
    private List<GridPosition> path;
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

    WaveSimulation(PrintStream out, ConsoleMapRenderer renderer, MatchSaver saver,
            EnemyCatalog enemyCatalog, TowerCatalog towerCatalog) {
        this.out = out;
        this.renderer = renderer;
        this.saver = saver;
        this.enemyCatalog = enemyCatalog;
        this.towerCatalog = towerCatalog;
    }

    void run(List<String> commands) {
        loadMap();
        loadWave();
        for (String command : commands) {
            handleCommand(command);
        }
        saver.save(tick, bits, nodeIntegrity);
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
            out.println("REJECTED place " + kind + ": not enough bits");
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
        out.println("PLACED " + kind + " at " + x + "," + y + " bits=" + bits);
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
        EnemyBehavior behavior = enemyCatalog.behaviorFor(type);
        Enemy enemy = new Enemy();
        enemy.id = nextEnemyId++;
        enemy.type = type;
        enemy.behavior = behavior;
        behavior.configure(enemy);
        enemy.progress = random.nextInt(1);
        enemies.add(enemy);
        out.println("SPAWN " + type + " id=" + enemy.id + " tick=" + tick);
    }

    private void moveEnemies() {
        for (Enemy enemy : enemies) {
            if (enemy.disinfected || enemy.leaked) {
                continue;
            }
            enemy.ageTicks++;
            enemy.progress += enemy.speed / 10.0;
            if (enemy.progress >= path.size() - 1) {
                enemy.leaked = true;
                nodeIntegrity -= enemy.leak;
                out.println("LEAK " + enemy.type + " id=" + enemy.id
                        + " integrity=" + nodeIntegrity);
            }
        }
    }

    private void applyEnemyAbilities() {
        for (Enemy enemy : enemies) {
            if (enemy.disinfected || enemy.leaked) {
                continue;
            }
            enemy.behavior.applyAbility(enemy, this);
        }
        enemies.addAll(pendingSpawns);
        pendingSpawns.clear();
    }

    private void updateTowers() {
        for (Tower tower : towers) {
            tower.behavior.update(tower, enemies, path, this);
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
        EnemyBehavior behavior = enemyCatalog.behaviorFor(type);
        Enemy enemy = new Enemy();
        enemy.id = nextEnemyId++;
        enemy.type = type;
        enemy.behavior = behavior;
        behavior.configure(enemy);
        enemy.progress = progress;
        pendingSpawns.add(enemy);
        out.println("SPAWN " + type + " id=" + enemy.id + " tick=" + tick);
    }

    @Override
    public int awardBounty(int bounty) {
        bits += bounty;
        return bits;
    }

    private void loadMap() {
        List<String> lines = readResourceLines("/map-r1.txt");
        map = new char[HEIGHT][WIDTH];
        path = new ArrayList<>();
        GridPosition spawn = null;
        GridPosition node = null;
        for (int y = 0; y < HEIGHT; y++) {
            char[] row = lines.get(y).toCharArray();
            for (int x = 0; x < WIDTH; x++) {
                map[y][x] = row[x];
                if (row[x] == 'S') {
                    spawn = new GridPosition(x, y);
                } else if (row[x] == 'N') {
                    node = new GridPosition(x, y);
                }
            }
        }
        buildPath(spawn, node);
    }

    private void buildPath(GridPosition spawn, GridPosition node) {
        int y = spawn.y();
        for (int x = spawn.x(); x <= node.x(); x++) {
            path.add(new GridPosition(x, y));
        }
    }

    private void loadWave() {
        for (String line : readResourceLines("/wave-r1.txt")) {
            if (line.isBlank()) {
                continue;
            }
            String[] parts = line.trim().split("\\s+");
            String type = parts[0];
            int count = Integer.parseInt(parts[1]);
            int interval = Integer.parseInt(parts[2]);
            int delay = Integer.parseInt(parts[3]);
            for (int i = 0; i < count; i++) {
                SpawnOrder order = new SpawnOrder();
                order.tick = delay + i * interval + 1;
                order.type = type;
                spawnSchedule.add(order);
            }
        }
    }

    private List<String> readResourceLines(String resource) {
        try (InputStream in = WaveSimulation.class.getResourceAsStream(resource)) {
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
