package dev.erizo.firewall.infrastructure.content;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.erizo.firewall.content.MissionDefinition;
import dev.erizo.firewall.content.MissionSource;
import dev.erizo.firewall.content.WaveDefinition;
import dev.erizo.firewall.content.WaveGroupDefinition;
import dev.erizo.firewall.content.WaveSource;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;

/** Loads wave and mission content from JSON classpath resources. */
public final class JsonContentSource implements WaveSource, MissionSource {

    private static final String DEFAULT_MISSIONS_PATH = "content/missions.json";

    private final ObjectMapper objectMapper;
    private final String wavesPath;
    private final String missionsPath;

    public JsonContentSource(String wavesPath) {
        this(wavesPath, DEFAULT_MISSIONS_PATH);
    }

    public JsonContentSource(String wavesPath, String missionsPath) {
        this.objectMapper = new ObjectMapper();
        this.wavesPath = wavesPath;
        this.missionsPath = missionsPath;
    }

    @Override
    public List<WaveDefinition> loadWaves() {
        List<WaveFileDto> dtos = readList(wavesPath, new TypeReference<List<WaveFileDto>>() {});
        return dtos.stream()
                .sorted(Comparator.comparingInt(WaveFileDto::number))
                .map(JsonContentSource::toWaveDefinition)
                .toList();
    }

    @Override
    public List<MissionDefinition> loadMissions() {
        List<MissionFileDto> dtos = readList(missionsPath, new TypeReference<List<MissionFileDto>>() {});
        return dtos.stream().map(JsonContentSource::toMissionDefinition).toList();
    }

    private <T> List<T> readList(String resourcePath, TypeReference<List<T>> type) {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (stream == null) {
                throw new ContentLoadException("Content resource not found: " + resourcePath);
            }
            return objectMapper.readValue(stream, type);
        } catch (java.io.IOException cause) {
            throw new ContentLoadException("Could not read content: " + resourcePath, cause);
        }
    }

    private static WaveDefinition toWaveDefinition(WaveFileDto dto) {
        List<WaveGroupDefinition> groups = dto.groups().stream()
                .map(group -> new WaveGroupDefinition(
                        group.enemyType(), group.count(), group.intervalTicks(), group.delayTicks()))
                .toList();
        return new WaveDefinition(dto.number(), groups);
    }

    private static MissionDefinition toMissionDefinition(MissionFileDto dto) {
        return new MissionDefinition(dto.id(), dto.type(), dto.timeLimitTicks(), dto.items());
    }
}
