package dev.erizo.firewall.defense;

import dev.erizo.firewall.content.WaveDefinition;
import java.util.List;

/** Sequences the match's waves, spawning each and gapping build time between them. */
final class WaveSchedule {

    private final List<Wave> waves;
    private int currentIndex;
    private int currentStartTick;
    private int wavesCleared;

    WaveSchedule(List<WaveDefinition> definitions, WaveBuilder builder) {
        this.waves = definitions.stream().map(builder::build).toList();
        this.currentIndex = 0;
        this.currentStartTick = 0;
        this.wavesCleared = 0;
    }

    /** Spawns into the match whatever the current wave releases at this tick. */
    void spawnDue(DefenseMatch match, EnemyFactory factory, int tick) {
        if (!hasCurrentWave() || tick < currentStartTick) {
            return;
        }
        current().spawnAt(tick - currentStartTick, factory).forEach(match::addEnemy);
    }

    /** Advances to the next wave once the current one is fully spawned and cleared. */
    void advance(DefenseMatch match, int tick) {
        if (!hasCurrentWave() || tick < currentStartTick) {
            return;
        }
        if (!current().fullySpawned(tick - currentStartTick)) {
            return;
        }
        if (!match.activeEnemies().isEmpty()) {
            return;
        }
        int clearedNumber = current().number();
        wavesCleared++;
        currentIndex++;
        currentStartTick = tick + Wave.BUILD_TIME_TICKS;
        match.publish(new WaveCleared(clearedNumber));
    }

    boolean allWavesCleared() {
        return currentIndex >= waves.size();
    }

    int wavesCleared() {
        return wavesCleared;
    }

    private boolean hasCurrentWave() {
        return currentIndex < waves.size();
    }

    private Wave current() {
        return waves.get(currentIndex);
    }
}
