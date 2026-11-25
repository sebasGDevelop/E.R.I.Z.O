package dev.erizo.firewall.defense;

import java.util.List;

/** The per-tick action a tower performs on the enemies within its range. */
public interface TowerBehavior {

    /** Acts on the in-range enemies for one tick, given the tower and the path. */
    void act(Tower tower, List<Enemy> enemiesInRange, EnemyPath path);
}
