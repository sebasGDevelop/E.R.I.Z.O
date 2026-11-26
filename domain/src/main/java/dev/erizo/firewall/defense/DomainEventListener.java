package dev.erizo.firewall.defense;

/** Reacts to the events a match publishes, overriding only the ones it cares about. */
public interface DomainEventListener {

    default void onEnemyDisinfected(EnemyDisinfected event) {
    }

    default void onTowerPlaced(TowerPlaced event) {
    }

    default void onWaveCleared(WaveCleared event) {
    }

    default void onNodeDamaged(NodeDamaged event) {
    }

    default void onMissionFinished(MissionFinished event) {
    }
}
