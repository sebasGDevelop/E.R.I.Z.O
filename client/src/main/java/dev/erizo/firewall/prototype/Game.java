package dev.erizo.firewall.prototype;

import dev.erizo.firewall.prototype.adapter.CommandReader;
import dev.erizo.firewall.prototype.adapter.ConsoleOutput;
import dev.erizo.firewall.prototype.adapter.FileMatchStorage;
import dev.erizo.firewall.prototype.adapter.ResourceMapLoader;
import dev.erizo.firewall.prototype.adapter.ResourceWaveSource;
import dev.erizo.firewall.prototype.adapter.SeededRandomSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Wires the console adapters and runs one defense wave from a command script.
 */
public final class Game {

    private final PrintStream out;

    public Game(PrintStream out) {
        this.out = out;
    }

    public void run(InputStream commandScript) {
        SimulationOutput output = new ConsoleOutput(out);
        RandomSource random = new SeededRandomSource(42);
        MatchStorage storage = new FileMatchStorage(Path.of("build/prototype-save.txt"));
        WaveSource waveSource = new ResourceWaveSource("/wave-r1.txt");
        MapData mapData = new ResourceMapLoader("/map-r1.txt").load();

        ConsoleMapRenderer renderer = new ConsoleMapRenderer(output);
        WormyBehavior wormy = new WormyBehavior(output);
        TrojyBehavior trojy = new TrojyBehavior(output);
        PhisherBehavior phisher = new PhisherBehavior();
        EnemyCatalog enemyCatalog = new EnemyCatalog(
                List.of(wormy, trojy, phisher),
                Map.of(wormy.type(), wormy, trojy.type(), trojy));
        TowerCatalog towerCatalog = new TowerCatalog(
                List.of(new SpikeWallBehavior(output)));

        WaveSimulation simulation = new WaveSimulation(output, random, storage, renderer,
                enemyCatalog, towerCatalog, mapData, waveSource.load());
        simulation.run(new CommandReader().read(commandScript));
    }

    public static void main(String[] args) {
        try (InputStream script = Game.class.getResourceAsStream("/scripts/wave-42.txt")) {
            new Game(System.out).run(script);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
