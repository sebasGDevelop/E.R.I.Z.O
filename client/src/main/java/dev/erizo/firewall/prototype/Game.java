package dev.erizo.firewall.prototype;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Coupled console prototype of one defense wave kept intentionally in a single class.
 */
public final class Game {

    private static final int WIDTH = 12;
    private static final int HEIGHT = 8;
    private static final int NODE_START_INTEGRITY = 20;
    private static final int STARTING_BITS = 100;
    private static final int SPIKE_COST = 40;
    private static final int SPIKE_RANGE = 2;
    private static final int SPIKE_COOLDOWN = 10;
    private static final int SPIKE_DAMAGE = 3;
    private static final int WORMY_DUPLICATE_TICK = 60;

    private final PrintStream out;
    private final Random random = new Random(42);

    private char[][] map;
    private List<int[]> path;
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Tower> towers = new ArrayList<>();

    private int tick;
    private int bits = STARTING_BITS;
    private int nodeIntegrity = NODE_START_INTEGRITY;
    private int nextEnemyId;
    private final List<SpawnOrder> spawnSchedule = new ArrayList<>();

    private static final class Enemy {
        int id;
        String type;
        double integrity;
        double speed;
        int leak;
        int bounty;
        double progress;
        int ageTicks;
        boolean duplicated;
        boolean released;
        boolean disinfected;
        boolean leaked;
    }

    private static final class Tower {
        String type;
        int x;
        int y;
        int cooldownLeft;
    }

    private static final class SpawnOrder {
        int tick;
        String type;
    }

    public Game(PrintStream out) {
        this.out = out;
    }

    public void run(InputStream commandScript) {
        loadMap();
        loadWave();
        for (String command : readCommands(commandScript)) {
            handleCommand(command);
        }
        save();
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
        if (kind.equals("SPIKE")) {
            if (bits < SPIKE_COST) {
                out.println("REJECTED place SPIKE: not enough bits");
                return;
            }
            Tower tower = new Tower();
            tower.type = "SPIKE";
            tower.x = x;
            tower.y = y;
            tower.cooldownLeft = 0;
            towers.add(tower);
            bits -= SPIKE_COST;
            out.println("PLACED SPIKE at " + x + "," + y + " bits=" + bits);
        }
    }

    private void advanceTick() {
        tick++;
        spawnDueEnemies();
        moveEnemies();
        applyEnemyAbilities();
        fireTowers();
        removeFinishedEnemies();
        if (tick % 10 == 0) {
            printMap();
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
        Enemy enemy = new Enemy();
        enemy.id = nextEnemyId++;
        enemy.type = type;
        if (type.equals("WORMY")) {
            enemy.integrity = 6;
            enemy.speed = 1.0;
            enemy.leak = 1;
            enemy.bounty = 5;
        } else if (type.equals("TROJY")) {
            enemy.integrity = 10;
            enemy.speed = 0.8;
            enemy.leak = 2;
            enemy.bounty = 10;
        }
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
        List<Enemy> spawned = new ArrayList<>();
        for (Enemy enemy : enemies) {
            if (enemy.disinfected || enemy.leaked) {
                continue;
            }
            if (enemy.type.equals("WORMY") && !enemy.duplicated
                    && enemy.ageTicks >= WORMY_DUPLICATE_TICK) {
                enemy.duplicated = true;
                spawned.add(copyAt("WORMY", enemy.progress));
                out.println("DUPLICATE WORMY id=" + enemy.id);
            } else if (enemy.type.equals("TROJY") && !enemy.released
                    && enemy.progress >= (path.size() - 1) / 2.0) {
                enemy.released = true;
                spawned.add(copyAt("WORMY", enemy.progress));
                spawned.add(copyAt("WORMY", enemy.progress));
                out.println("OPEN TROJY id=" + enemy.id);
            }
        }
        enemies.addAll(spawned);
    }

    private Enemy copyAt(String type, double progress) {
        Enemy enemy = new Enemy();
        enemy.id = nextEnemyId++;
        enemy.type = type;
        enemy.integrity = 6;
        enemy.speed = 1.0;
        enemy.leak = 1;
        enemy.bounty = 5;
        enemy.progress = progress;
        out.println("SPAWN " + type + " id=" + enemy.id + " tick=" + tick);
        return enemy;
    }

    private void fireTowers() {
        for (Tower tower : towers) {
            if (tower.cooldownLeft > 0) {
                tower.cooldownLeft--;
                continue;
            }
            if (!tower.type.equals("SPIKE")) {
                continue;
            }
            Enemy target = pickFirstInRange(tower, SPIKE_RANGE);
            if (target == null) {
                continue;
            }
            target.integrity -= SPIKE_DAMAGE;
            tower.cooldownLeft = SPIKE_COOLDOWN;
            out.println("HIT id=" + target.id + " integrity=" + target.integrity);
            if (target.integrity <= 0) {
                target.disinfected = true;
                bits += target.bounty;
                out.println("DISINFECT id=" + target.id + " bits=" + bits);
            }
        }
    }

    private Enemy pickFirstInRange(Tower tower, int range) {
        Enemy best = null;
        for (Enemy enemy : enemies) {
            if (enemy.disinfected || enemy.leaked) {
                continue;
            }
            int[] tile = path.get((int) Math.floor(enemy.progress));
            double dx = tile[0] - tower.x;
            double dy = tile[1] - tower.y;
            if (Math.sqrt(dx * dx + dy * dy) > range) {
                continue;
            }
            if (best == null || enemy.progress > best.progress
                    || (enemy.progress == best.progress && enemy.id < best.id)) {
                best = enemy;
            }
        }
        return best;
    }

    private void removeFinishedEnemies() {
        enemies.removeIf(enemy -> enemy.disinfected || enemy.leaked);
    }

    private void printMap() {
        char[][] frame = new char[HEIGHT][WIDTH];
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                frame[y][x] = map[y][x];
            }
        }
        for (Tower tower : towers) {
            frame[tower.y][tower.x] = 'T';
        }
        for (Enemy enemy : enemies) {
            int[] tile = path.get((int) Math.floor(enemy.progress));
            frame[tile[1]][tile[0]] = 'e';
        }
        out.println("--- tick " + tick + " bits=" + bits
                + " integrity=" + nodeIntegrity + " ---");
        for (int y = 0; y < HEIGHT; y++) {
            out.println(new String(frame[y]));
        }
    }

    private void loadMap() {
        List<String> lines = readResourceLines("/map-r1.txt");
        map = new char[HEIGHT][WIDTH];
        path = new ArrayList<>();
        int[] spawn = null;
        int[] node = null;
        for (int y = 0; y < HEIGHT; y++) {
            char[] row = lines.get(y).toCharArray();
            for (int x = 0; x < WIDTH; x++) {
                map[y][x] = row[x];
                if (row[x] == 'S') {
                    spawn = new int[] {x, y};
                } else if (row[x] == 'N') {
                    node = new int[] {x, y};
                }
            }
        }
        buildPath(spawn, node);
    }

    private void buildPath(int[] spawn, int[] node) {
        int y = spawn[1];
        for (int x = spawn[0]; x <= node[0]; x++) {
            path.add(new int[] {x, y});
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
        try (InputStream in = Game.class.getResourceAsStream(resource)) {
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

    private List<String> readCommands(InputStream in) {
        List<String> commands = new ArrayList<>();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(in, StandardCharsets.UTF_8));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    commands.add(line);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return commands;
    }

    private void save() {
        String snapshot = "tick=" + tick + " bits=" + bits
                + " integrity=" + nodeIntegrity;
        try {
            Path target = Path.of("build/prototype-save.txt");
            Files.createDirectories(target.getParent());
            Files.writeString(target, snapshot);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        out.println("SAVED " + snapshot);
    }

    public static void main(String[] args) {
        try (InputStream script = Game.class.getResourceAsStream("/scripts/wave-42.txt")) {
            new Game(System.out).run(script);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
