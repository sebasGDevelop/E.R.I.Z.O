package dev.erizo.firewall.prototype;

/**
 * The slice of simulation state that enemy abilities read and extend.
 */
interface WaveState {

    int pathLength();

    void spawnOffspring(String type, double progress);
}
