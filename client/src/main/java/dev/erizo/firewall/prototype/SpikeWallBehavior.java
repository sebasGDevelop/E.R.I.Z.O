package dev.erizo.firewall.prototype;

import java.io.PrintStream;
import java.util.List;

/**
 * Spike Wall: hits the enemy farthest along the path within range for 3 damage.
 */
final class SpikeWallBehavior implements TowerBehavior {

    private static final int RANGE = 2;
    private static final int DAMAGE = 3;

    private final PrintStream out;

    SpikeWallBehavior(PrintStream out) {
        this.out = out;
    }

    @Override
    public String type() {
        return "SPIKE";
    }

    @Override
    public int cost() {
        return 40;
    }

    @Override
    public int cooldown() {
        return 10;
    }

    @Override
    public void fire(Tower tower, List<Enemy> enemies, List<GridPosition> path,
            TowerContext context) {
        Enemy target = pickFirstInRange(tower, enemies, path);
        if (target == null) {
            return;
        }
        target.integrity -= DAMAGE;
        tower.cooldownLeft = cooldown();
        out.println("HIT id=" + target.id + " integrity=" + target.integrity);
        if (target.integrity <= 0) {
            target.disinfected = true;
            int bits = context.awardBounty(target.bounty);
            out.println("DISINFECT id=" + target.id + " bits=" + bits);
        }
    }

    private Enemy pickFirstInRange(Tower tower, List<Enemy> enemies, List<GridPosition> path) {
        Enemy best = null;
        for (Enemy enemy : enemies) {
            if (enemy.disinfected || enemy.leaked) {
                continue;
            }
            GridPosition tile = path.get((int) Math.floor(enemy.progress));
            double dx = tile.x() - tower.x;
            double dy = tile.y() - tower.y;
            if (Math.sqrt(dx * dx + dy * dy) > RANGE) {
                continue;
            }
            if (best == null || enemy.progress > best.progress
                    || (enemy.progress == best.progress && enemy.id < best.id)) {
                best = enemy;
            }
        }
        return best;
    }
}
