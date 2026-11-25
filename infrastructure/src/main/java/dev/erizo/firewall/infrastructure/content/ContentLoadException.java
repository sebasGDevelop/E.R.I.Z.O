package dev.erizo.firewall.infrastructure.content;

/** Thrown when content cannot be read or parsed from its source. */
public class ContentLoadException extends RuntimeException {

    public ContentLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContentLoadException(String message) {
        super(message);
    }
}
