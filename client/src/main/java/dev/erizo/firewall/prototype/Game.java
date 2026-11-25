package dev.erizo.firewall.prototype;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;

/**
 * Wires the console collaborators and runs one defense wave from a command script.
 */
public final class Game {

    private final PrintStream out;

    public Game(PrintStream out) {
        this.out = out;
    }

    public void run(InputStream commandScript) {
        CommandReader commandReader = new CommandReader();
        ConsoleMapRenderer renderer = new ConsoleMapRenderer(out);
        MatchSaver saver = new MatchSaver(out);
        WormyBehavior wormy = new WormyBehavior(out);
        TrojyBehavior trojy = new TrojyBehavior(out);
        EnemyCatalog enemyCatalog = new EnemyCatalog(
                List.of(wormy, trojy),
                Map.of(wormy.type(), wormy, trojy.type(), trojy));
        TowerCatalog towerCatalog = new TowerCatalog(
                List.of(new SpikeWallBehavior(out)));
        WaveSimulation simulation = new WaveSimulation(
                out, renderer, saver, enemyCatalog, towerCatalog);
        simulation.run(commandReader.read(commandScript));
    }

    public static void main(String[] args) {
        try (InputStream script = Game.class.getResourceAsStream("/scripts/wave-42.txt")) {
            new Game(System.out).run(script);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
