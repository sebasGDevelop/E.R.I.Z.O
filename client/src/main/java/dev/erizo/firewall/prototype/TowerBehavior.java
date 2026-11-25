package dev.erizo.firewall.prototype;

import java.util.List;

/**
 * Per-type tower rules every tower honors: placement cost and a per-tick update.
 */
interface TowerBehavior {

    String type();

    int cost();

    void update(Tower tower, List<Enemy> enemies, List<GridPosition> path, TowerContext context);
}
