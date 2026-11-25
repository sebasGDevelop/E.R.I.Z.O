package dev.erizo.firewall.prototype.adapter;

import dev.erizo.firewall.prototype.WaveDefinition;
import dev.erizo.firewall.prototype.WaveEntry;
import dev.erizo.firewall.prototype.WaveSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads a wave definition from a classpath resource.
 */
public final class ResourceWaveSource implements WaveSource {

    private final String resource;

    public ResourceWaveSource(String resource) {
        this.resource = resource;
    }

    @Override
    public WaveDefinition load() {
        List<WaveEntry> entries = new ArrayList<>();
        for (String line : readLines()) {
            if (line.isBlank()) {
                continue;
            }
            String[] parts = line.trim().split("\\s+");
            entries.add(new WaveEntry(parts[0], Integer.parseInt(parts[1]),
                    Integer.parseInt(parts[2]), Integer.parseInt(parts[3])));
        }
        return new WaveDefinition(1, List.copyOf(entries));
    }

    private List<String> readLines() {
        try (InputStream in = ResourceWaveSource.class.getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("missing resource " + resource);
            }
            List<String> lines = new ArrayList<>();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(in, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            return lines;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
