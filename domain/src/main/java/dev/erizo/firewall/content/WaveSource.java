package dev.erizo.firewall.content;

import java.util.List;

/** Supplies the wave definitions that make up a match, in order. */
public interface WaveSource {

    /** Loads every wave definition, ordered by wave number. */
    List<WaveDefinition> loadWaves();
}
