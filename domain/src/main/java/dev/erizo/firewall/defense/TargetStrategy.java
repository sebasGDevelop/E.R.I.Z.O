package dev.erizo.firewall.defense;

import dev.erizo.firewall.shared.GridPosition;
import java.util.List;
import java.util.Optional;

/** Chooses which in-range enemy a tower fires at. */
public interface TargetStrategy {

    /** Picks one enemy from the candidates already filtered to those in range. */
    Optional<Enemy> choose(List<Enemy> candidates, GridPosition towerPosition, EnemyPath path);
}
