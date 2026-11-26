package dev.erizo.firewall.defense;

/** Published when every enemy of a wave has been resolved. */
public record WaveCleared(int waveNumber) implements DomainEvent {

    @Override
    public void dispatchTo(DomainEventListener listener) {
        listener.onWaveCleared(this);
    }
}
