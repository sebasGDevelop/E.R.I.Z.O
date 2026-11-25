package dev.erizo.firewall.prototype;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class WaveSimulationTest {

    private final RecordingOutput output = new RecordingOutput();

    @Test
    void run_whenPlacingTowerWithEnoughBits_reportsPlacement() {
        WaveSimulation simulation = simulationWith(waveOf());

        simulation.run(List.of("PLACE SPIKE 0 1"));

        assertThat(output.lines()).contains("PLACED SPIKE at 0,1 bits=60");
    }

    @Test
    void run_whenPlacingTowerWithoutEnoughBits_reportsRejection() {
        WaveSimulation simulation = simulationWith(waveOf());

        simulation.run(List.of("PLACE SPIKE 0 1", "PLACE SPIKE 1 1", "PLACE SPIKE 2 1"));

        assertThat(output.lines()).contains("REJECTED place SPIKE: not enough bits");
    }

    @Test
    void run_whenWormyReachesNode_reducesNodeIntegrity() {
        WaveSimulation simulation = simulationWith(waveOf(new WaveEntry("WORMY", 1, 0, 0)));

        simulation.run(List.of("RUN 40"));

        assertThat(output.lines()).contains("LEAK WORMY id=0 integrity=19");
    }

    @Test
    void run_whenSpikeWallInRange_disinfectsWormy() {
        WaveSimulation simulation = simulationWith(waveOf(new WaveEntry("WORMY", 1, 0, 0)));

        simulation.run(List.of("PLACE SPIKE 0 1", "RUN 20"));

        assertThat(output.lines()).contains("DISINFECT id=0 bits=65");
    }

    @Test
    void run_always_savesFinalSnapshotThroughStorage() {
        MatchStorage storage = Mockito.mock(MatchStorage.class);
        WaveSimulation simulation = simulationWith(waveOf(), storage);

        simulation.run(List.of("RUN 5"));

        ArgumentCaptor<MatchSnapshot> captured = ArgumentCaptor.forClass(MatchSnapshot.class);
        verify(storage).save(captured.capture());
        assertThat(captured.getValue()).isEqualTo(new MatchSnapshot(5, 100, 20));
    }

    private WaveSimulation simulationWith(WaveDefinition wave) {
        return simulationWith(wave, Mockito.mock(MatchStorage.class));
    }

    private WaveSimulation simulationWith(WaveDefinition wave, MatchStorage storage) {
        WormyBehavior wormy = new WormyBehavior(output);
        TrojyBehavior trojy = new TrojyBehavior(output);
        EnemyCatalog enemyCatalog = new EnemyCatalog(
                List.of(wormy, trojy),
                Map.of(wormy.type(), wormy, trojy.type(), trojy));
        TowerCatalog towerCatalog = new TowerCatalog(List.of(new SpikeWallBehavior(output)));
        ConsoleMapRenderer renderer = new ConsoleMapRenderer(output);
        return new WaveSimulation(output, new FixedRandomSource(0), storage, renderer,
                enemyCatalog, towerCatalog, straightPath(), wave);
    }

    private MapData straightPath() {
        char[][] grid = {
            "S..N".toCharArray(),
            "____".toCharArray()
        };
        List<GridPosition> path = List.of(
                new GridPosition(0, 0), new GridPosition(1, 0),
                new GridPosition(2, 0), new GridPosition(3, 0));
        return new MapData(grid, path);
    }

    private WaveDefinition waveOf(WaveEntry... entries) {
        return new WaveDefinition(1, List.of(entries));
    }
}
