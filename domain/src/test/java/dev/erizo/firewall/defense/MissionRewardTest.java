package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;

import dev.erizo.firewall.shared.GridPosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class MissionRewardTest {

    private static MapLayout map = buildMap();

    private static MapLayout buildMap() {
        List<List<TileType>> rows = new ArrayList<>();
        for (int y = 0; y < MapLayout.HEIGHT; y++) {
            rows.add(new ArrayList<>(Collections.nCopies(MapLayout.WIDTH, TileType.BUILD)));
        }
        for (int x = 0; x < MapLayout.WIDTH; x++) {
            rows.get(0).set(x, TileType.PATH);
        }
        rows.get(0).set(0, TileType.SPAWN);
        rows.get(0).set(MapLayout.WIDTH - 1, TileType.NODE);
        return MapLayout.fromRows(rows);
    }

    private static EnemyPath straightPath() {
        List<GridPosition> steps = new ArrayList<>();
        for (int x = 0; x < MapLayout.WIDTH; x++) {
            steps.add(new GridPosition(x, 0));
        }
        return EnemyPath.over(map, steps);
    }

    @Test
    void applyMissionReward_whenCompleted_addsTheMissionBits() {
        DefenseMatch match = DefenseMatch.start(map, straightPath());

        match.applyMissionReward("intercept", true, 40);

        assertThat(match.bits().amount()).isEqualTo(140);
    }

    @Test
    void applyMissionReward_whenFailed_leavesBitsUnchanged() {
        DefenseMatch match = DefenseMatch.start(map, straightPath());

        match.applyMissionReward("intercept", false, 40);

        assertThat(match.bits().amount()).isEqualTo(100);
    }

    @Test
    void applyMissionReward_publishesMissionFinished() {
        DomainEventPublisher publisher = new DomainEventPublisher();
        List<String> finished = new ArrayList<>();
        publisher.register(new DomainEventListener() {
            @Override
            public void onMissionFinished(MissionFinished event) {
                finished.add(event.missionId());
            }
        });
        DefenseMatch match = DefenseMatch.start(map, straightPath(), publisher);

        match.applyMissionReward("intercept", true, 40);

        assertThat(finished).containsExactly("intercept");
    }
}
