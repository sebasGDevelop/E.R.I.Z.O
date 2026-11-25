package dev.erizo.firewall.defense;

/** Thrown when a tower cannot be placed on the chosen tile. */
public class InvalidPlacementException extends RuntimeException {

    public InvalidPlacementException(String message) {
        super(message);
    }
}
