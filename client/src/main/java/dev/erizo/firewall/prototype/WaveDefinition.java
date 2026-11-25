package dev.erizo.firewall.prototype;

import java.util.List;

/**
 * The enemies a wave spawns, as data loaded through a port.
 */
public record WaveDefinition(int number, List<WaveEntry> entries) {
}
