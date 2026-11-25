package dev.erizo.firewall.prototype;

/**
 * The end-of-run state of a match persisted through a port.
 */
public record MatchSnapshot(int tick, int bits, int nodeIntegrity) {
}
