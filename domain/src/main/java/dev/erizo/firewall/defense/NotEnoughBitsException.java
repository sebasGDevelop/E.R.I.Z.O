package dev.erizo.firewall.defense;

/** Thrown when a player action costs more bits than are available. */
public class NotEnoughBitsException extends RuntimeException {

    public NotEnoughBitsException(String message) {
        super(message);
    }
}
