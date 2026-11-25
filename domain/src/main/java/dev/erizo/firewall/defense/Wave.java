package dev.erizo.firewall.defense;

import java.util.List;

/** One wave of the match: a tree of groups that releases enemies over its ticks. */
public final class Wave {

    public static final int BUILD_TIME_TICKS = 300;

    private final int number;
    private final WaveComponent root;

    public Wave(int number, WaveComponent root) {
        this.number = number;
        this.root = root;
    }

    /** Creates the enemies due to spawn at the given tick through the factory. */
    public List<Enemy> spawnAt(int tick, EnemyFactory factory) {
        return root.spawnsAt(tick).stream().map(factory::create).toList();
    }

    /** Whether every enemy of this wave has been released by the given tick. */
    public boolean fullySpawned(int tick) {
        return tick >= root.lastSpawnTick();
    }

    public int number() {
        return number;
    }

    public int totalEnemies() {
        return root.totalSpawns();
    }
}
