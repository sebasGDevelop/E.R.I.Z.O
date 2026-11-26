package dev.erizo.firewall.missions;

import java.util.List;

/** A short spy mission offered between waves that teaches a security idea. */
public interface Mission {

    /** The mission's stable content id. */
    String id();

    /** The question shown to the player. */
    String prompt();

    /** The options the player chooses from, in order. */
    List<String> options();

    /** Evaluates the chosen option index and returns the outcome with its explanation. */
    MissionResult evaluate(int chosenOption);
}
