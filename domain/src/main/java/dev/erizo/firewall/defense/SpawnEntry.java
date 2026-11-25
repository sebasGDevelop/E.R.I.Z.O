package dev.erizo.firewall.defense;

import java.util.List;

/** A leaf of the wave tree: one enemy released at one tick. */
public final class SpawnEntry implements WaveComponent {

    private final String enemyType;
    private final int tick;

    public SpawnEntry(String enemyType, int tick) {
        this.enemyType = enemyType;
        this.tick = tick;
    }

    @Override
    public List<String> spawnsAt(int atTick) {
        return atTick == tick ? List.of(enemyType) : List.of();
    }

    @Override
    public int lastSpawnTick() {
        return tick;
    }

    @Override
    public int totalSpawns() {
        return 1;
    }
}
