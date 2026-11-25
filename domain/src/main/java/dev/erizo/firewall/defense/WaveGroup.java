package dev.erizo.firewall.defense;

import java.util.ArrayList;
import java.util.List;

/** A composite of wave nodes, aggregating what its children spawn. */
public final class WaveGroup implements WaveComponent {

    private final List<WaveComponent> children;

    public WaveGroup(List<WaveComponent> children) {
        this.children = List.copyOf(children);
    }

    @Override
    public List<String> spawnsAt(int tick) {
        List<String> spawns = new ArrayList<>();
        for (WaveComponent child : children) {
            spawns.addAll(child.spawnsAt(tick));
        }
        return List.copyOf(spawns);
    }

    @Override
    public int lastSpawnTick() {
        return children.stream().mapToInt(WaveComponent::lastSpawnTick).max().orElse(0);
    }

    @Override
    public int totalSpawns() {
        return children.stream().mapToInt(WaveComponent::totalSpawns).sum();
    }
}
