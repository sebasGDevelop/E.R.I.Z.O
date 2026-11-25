package dev.erizo.firewall.infrastructure.content;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.erizo.firewall.content.WaveDefinition;
import dev.erizo.firewall.content.WaveGroupDefinition;
import dev.erizo.firewall.content.WaveSource;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;

/** Loads wave content from a JSON classpath resource. */
public final class JsonContentSource implements WaveSource {

    private final ObjectMapper objectMapper;
    private final String resourcePath;

    public JsonContentSource(String resourcePath) {
        this.objectMapper = new ObjectMapper();
        this.resourcePath = resourcePath;
    }

    @Override
    public List<WaveDefinition> loadWaves() {
        List<WaveFileDto> dtos = readWaveFile();
        return dtos.stream()
                .sorted(Comparator.comparingInt(WaveFileDto::number))
                .map(JsonContentSource::toDefinition)
                .toList();
    }

    private List<WaveFileDto> readWaveFile() {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (stream == null) {
                throw new ContentLoadException("Content resource not found: " + resourcePath);
            }
            return objectMapper.readValue(stream, new TypeReference<List<WaveFileDto>>() {});
        } catch (java.io.IOException cause) {
            throw new ContentLoadException("Could not read content: " + resourcePath, cause);
        }
    }

    private static WaveDefinition toDefinition(WaveFileDto dto) {
        List<WaveGroupDefinition> groups = dto.groups().stream()
                .map(group -> new WaveGroupDefinition(
                        group.enemyType(), group.count(), group.intervalTicks(), group.delayTicks()))
                .toList();
        return new WaveDefinition(dto.number(), groups);
    }
}
