package dev.erizo.firewall.defense;

/** The outcome of a match: waves cleared, node integrity, bits and score. */
public record MatchResult(int wavesCleared, int nodeIntegrity, int bits, long score) {
}
