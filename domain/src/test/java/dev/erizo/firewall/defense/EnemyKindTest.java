package dev.erizo.firewall.defense;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EnemyKindTest {

    @Test
    void stats_forWormy_matchGameRules() {
        EnemyStats stats = EnemyKind.WORMY.stats();

        assertThat(stats).isEqualTo(new EnemyStats(6, 0.10, 5, 1));
    }

    @Test
    void stats_forBuzzCloudMember_matchGameRules() {
        EnemyStats stats = EnemyKind.BUZZ_CLOUD_MEMBER.stats();

        assertThat(stats).isEqualTo(new EnemyStats(1, 0.15, 1, 1));
    }

    @Test
    void startsDisguised_forPhisher_isTrue() {
        assertThat(EnemyKind.PHISHER.startsDisguised()).isTrue();
    }

    @Test
    void startsDisguised_forWormy_isFalse() {
        assertThat(EnemyKind.WORMY.startsDisguised()).isFalse();
    }
}
