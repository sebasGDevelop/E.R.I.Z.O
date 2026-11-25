package dev.erizo.firewall.prototype.adapter;

import dev.erizo.firewall.prototype.MatchSnapshot;
import dev.erizo.firewall.prototype.MatchStorage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Persists the match snapshot to a file on disk.
 */
public final class FileMatchStorage implements MatchStorage {

    private final Path target;

    public FileMatchStorage(Path target) {
        this.target = target;
    }

    @Override
    public void save(MatchSnapshot snapshot) {
        String text = "tick=" + snapshot.tick() + " bits=" + snapshot.bits()
                + " integrity=" + snapshot.nodeIntegrity();
        try {
            Files.createDirectories(target.getParent());
            Files.writeString(target, text);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
