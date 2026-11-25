package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class EnemyFactoryTest {

    private final EnemyFactory factory = new EnemyFactory();

    @Test
    void create_forKnownType_producesEnemyOfThatKind() {
        Enemy enemy = factory.create("phisher");

        assertThat(enemy.kind()).isEqualTo(EnemyKind.PHISHER);
    }

    @Test
    void create_twice_assignsDistinctIds() {
        Enemy first = factory.create("wormy");
        Enemy second = factory.create("wormy");

        assertThat(second.id()).isNotEqualTo(first.id());
    }

    @Test
    void create_forUnknownType_isRejected() {
        assertThatThrownBy(() -> factory.create("dragon"))
                .isInstanceOf(UnknownEnemyTypeException.class);
    }
}
