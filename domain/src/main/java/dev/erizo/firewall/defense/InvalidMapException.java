package dev.erizo.firewall.defense;

/** Thrown when a map or its path does not satisfy the game's structure rules. */
public class InvalidMapException extends RuntimeException {

    public InvalidMapException(String message) {
        super(message);
    }
}
