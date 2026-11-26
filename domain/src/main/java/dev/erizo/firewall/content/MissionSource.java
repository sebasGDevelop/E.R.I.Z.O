package dev.erizo.firewall.content;

import java.util.List;

/** Supplies the spy mission definitions available between waves. */
public interface MissionSource {

    /** Loads every mission definition. */
    List<MissionDefinition> loadMissions();
}
