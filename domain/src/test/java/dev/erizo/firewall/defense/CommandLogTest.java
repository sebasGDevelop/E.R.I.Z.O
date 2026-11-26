package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;

import dev.erizo.firewall.shared.GridPosition;
import org.junit.jupiter.api.Test;

class CommandLogTest {

    private final CommandLog log = new CommandLog();

    @Test
    void commandsAt_returnsOnlyCommandsForThatTick() {
        log.record(new PlaceTowerCommand(5, TowerKind.SPIKE_WALL, new GridPosition(1, 1)));
        log.record(new PlaceTowerCommand(9, TowerKind.HONEYPOT, new GridPosition(2, 1)));

        assertThat(log.commandsAt(5)).hasSize(1);
    }

    @Test
    void commandsAt_preservesRecordedOrderWithinTick() {
        PlaceTowerCommand first = new PlaceTowerCommand(5, TowerKind.SPIKE_WALL, new GridPosition(1, 1));
        PlaceTowerCommand second = new PlaceTowerCommand(5, TowerKind.HONEYPOT, new GridPosition(2, 1));
        log.record(first);
        log.record(second);

        assertThat(log.commandsAt(5)).containsExactly(first, second);
    }

    @Test
    void lastCommandTick_isTheLatestScheduledTick() {
        log.record(new PlaceTowerCommand(5, TowerKind.SPIKE_WALL, new GridPosition(1, 1)));
        log.record(new PlaceTowerCommand(40, TowerKind.HONEYPOT, new GridPosition(2, 1)));

        assertThat(log.lastCommandTick()).isEqualTo(40);
    }

    @Test
    void lastCommandTick_whenEmpty_isZero() {
        assertThat(log.lastCommandTick()).isEqualTo(0);
    }
}
