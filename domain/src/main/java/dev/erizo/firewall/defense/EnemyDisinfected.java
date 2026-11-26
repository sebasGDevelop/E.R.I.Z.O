package dev.erizo.firewall.defense;

import dev.erizo.firewall.shared.EnemyId;

/** Published when an enemy is disinfected and its bounty is earned. */
public record EnemyDisinfected(EnemyId enemyId, int bounty) implements DomainEvent {

    @Override
    public void dispatchTo(DomainEventListener listener) {
        listener.onEnemyDisinfected(this);
    }
}
