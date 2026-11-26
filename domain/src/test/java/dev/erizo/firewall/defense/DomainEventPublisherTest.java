package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;

import dev.erizo.firewall.shared.EnemyId;
import dev.erizo.firewall.shared.GridPosition;
import dev.erizo.firewall.shared.TowerId;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class DomainEventPublisherTest {

    private static final class RecordingListener implements DomainEventListener {
        private final List<DomainEvent> received = new ArrayList<>();

        @Override
        public void onTowerPlaced(TowerPlaced event) {
            received.add(event);
        }

        @Override
        public void onEnemyDisinfected(EnemyDisinfected event) {
            received.add(event);
        }
    }

    @Test
    void publish_deliversEventToRegisteredListener() {
        DomainEventPublisher publisher = new DomainEventPublisher();
        RecordingListener listener = new RecordingListener();
        publisher.register(listener);

        publisher.publish(new TowerPlaced(new TowerId(1), TowerKind.SPIKE_WALL, new GridPosition(1, 1)));

        assertThat(listener.received).hasSize(1);
    }

    @Test
    void publish_dispatchesEventToItsMatchingHandler() {
        DomainEventPublisher publisher = new DomainEventPublisher();
        RecordingListener listener = new RecordingListener();
        publisher.register(listener);
        EnemyDisinfected event = new EnemyDisinfected(new EnemyId(7), 5);

        publisher.publish(event);

        assertThat(listener.received).containsExactly(event);
    }

    @Test
    void publish_ignoresEventTypesTheListenerDoesNotOverride() {
        DomainEventPublisher publisher = new DomainEventPublisher();
        RecordingListener listener = new RecordingListener();
        publisher.register(listener);

        publisher.publish(new WaveCleared(1));

        assertThat(listener.received).isEmpty();
    }
}
