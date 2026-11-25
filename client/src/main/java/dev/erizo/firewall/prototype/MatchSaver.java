package dev.erizo.firewall.prototype;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Writes the final match snapshot to disk and reports it on the console.
 */
final class MatchSaver {

    private final PrintStream out;

    MatchSaver(PrintStream out) {
        this.out = out;
    }

    void save(int tick, int bits, int nodeIntegrity) {
        String snapshot = "tick=" + tick + " bits=" + bits
                + " integrity=" + nodeIntegrity;
        try {
            Path target = Path.of("build/prototype-save.txt");
            Files.createDirectories(target.getParent());
            Files.writeString(target, snapshot);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        out.println("SAVED " + snapshot);
    }
}
