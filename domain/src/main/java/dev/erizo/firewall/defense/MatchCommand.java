package dev.erizo.firewall.defense;

/** A player action recorded with the tick it happens on, replayable on a match. */
public interface MatchCommand {

    /** The tick at which this command is applied. */
    int tick();

    /** Applies the command to the match. */
    void applyTo(DefenseMatch match);
}
