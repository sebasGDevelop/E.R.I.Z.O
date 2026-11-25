package dev.erizo.firewall.prototype;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

/**
 * Pins the console output of the prototype wave so refactors cannot change behavior.
 */
class GameCharacterizationTest {

    @Test
    void run_withSeed42AndWave42Script_matchesGoldenOutput() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Game game = new Game(new PrintStream(buffer, true, StandardCharsets.UTF_8));

        try (InputStream script = resource("/scripts/wave-42.txt")) {
            game.run(script);
        }

        assertThat(normalize(buffer.toString(StandardCharsets.UTF_8)))
                .isEqualTo(normalize(readGolden()));
    }

    private String readGolden() throws IOException {
        try (InputStream golden = resource("/golden/wave-42.txt")) {
            return new String(golden.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private InputStream resource(String path) {
        InputStream in = getClass().getResourceAsStream(path);
        if (in == null) {
            throw new IllegalStateException("missing resource " + path);
        }
        return in;
    }

    private String normalize(String text) {
        return text.replace("\r\n", "\n");
    }
}
