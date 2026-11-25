package dev.erizo.firewall.prototype;

/**
 * Sink for the lines the simulation reports as it runs.
 */
public interface SimulationOutput {

    void line(String text);
}
