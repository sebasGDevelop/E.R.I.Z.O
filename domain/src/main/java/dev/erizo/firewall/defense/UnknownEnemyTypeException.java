package dev.erizo.firewall.defense;

/** Thrown when content names an enemy type the game does not know. */
public class UnknownEnemyTypeException extends RuntimeException {

    public UnknownEnemyTypeException(String message) {
        super(message);
    }
}
