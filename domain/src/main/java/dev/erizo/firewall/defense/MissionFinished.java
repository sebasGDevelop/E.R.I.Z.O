package dev.erizo.firewall.defense;

/** Published when a spy mission ends, whether completed or failed. */
public record MissionFinished(String missionId, boolean success) implements DomainEvent {

    @Override
    public void dispatchTo(DomainEventListener listener) {
        listener.onMissionFinished(this);
    }
}
