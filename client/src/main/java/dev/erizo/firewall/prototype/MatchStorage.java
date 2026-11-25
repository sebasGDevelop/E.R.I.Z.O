package dev.erizo.firewall.prototype;

/**
 * Persists the final match snapshot.
 */
public interface MatchStorage {

    void save(MatchSnapshot snapshot);
}
