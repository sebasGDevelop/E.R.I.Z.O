package dev.erizo.firewall.defense;

import java.util.List;

/** A node in a wave's spawn tree, able to report what it spawns at a tick. */
public interface WaveComponent {

    /** The enemy type ids this node releases at the given tick. */
    List<String> spawnsAt(int tick);

    /** The last tick at which this node still releases an enemy. */
    int lastSpawnTick();

    /** How many enemies this node releases in total. */
    int totalSpawns();
}
