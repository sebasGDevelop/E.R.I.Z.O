package dev.erizo.firewall.prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * Test fake that records the lines the simulation emits.
 */
final class RecordingOutput implements SimulationOutput {

    private final List<String> lines = new ArrayList<>();

    @Override
    public void line(String text) {
        lines.add(text);
    }

    List<String> lines() {
        return List.copyOf(lines);
    }
}
