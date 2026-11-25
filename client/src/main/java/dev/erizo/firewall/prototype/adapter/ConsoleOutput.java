package dev.erizo.firewall.prototype.adapter;

import dev.erizo.firewall.prototype.SimulationOutput;
import java.io.PrintStream;

/**
 * Writes simulation lines to a console stream.
 */
public final class ConsoleOutput implements SimulationOutput {

    private final PrintStream out;

    public ConsoleOutput(PrintStream out) {
        this.out = out;
    }

    @Override
    public void line(String text) {
        out.println(text);
    }
}
