package dev.erizo.firewall.prototype;

import java.util.List;

/**
 * Per-type tower rules: placement cost and the effect applied each cooldown.
 */
interface TowerBehavior {

    String type();

    int cost();

    int cooldown();

    void fire(Tower tower, List<Enemy> enemies, List<GridPosition> path, TowerContext context);
}
