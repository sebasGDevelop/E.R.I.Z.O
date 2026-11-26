package dev.erizo.firewall.missions;

/** Thrown when content names a mission type the game does not know. */
public class UnknownMissionTypeException extends RuntimeException {

    public UnknownMissionTypeException(String message) {
        super(message);
    }
}
