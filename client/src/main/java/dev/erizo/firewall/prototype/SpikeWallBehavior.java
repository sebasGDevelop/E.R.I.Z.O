package dev.erizo.firewall.prototype;

import java.io.PrintStream;
import java.util.List;

/**
 * Spike Wall: hits the enemy farthest along the path within range for 3 damage.
 */
final class SpikeWallBehavior implements TowerBehavior {

    private static final int RANGE = 2;
    private static final int DAMAGE = 3;
    private static final int COOLDOWN = 10;

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
    public void update(Tower tower, List<Targetable> targets, List<GridPosition> path,
            TowerContext context) {
        if (tower.cooldownLeft > 0) {
            tower.cooldownLeft--;
            return;
        }
        Targetable target = pickFirstInRange(tower, targets, path);
        if (target == null) {
            return;
        }
        double integrity = target.applyDamage(DAMAGE);
        tower.cooldownLeft = COOLDOWN;
        out.println("HIT id=" + target.id() + " integrity=" + integrity);
        if (integrity <= 0) {
            target.disinfect();
            int bits = context.awardBounty(target.bounty());
            out.println("DISINFECT id=" + target.id() + " bits=" + bits);
        }
    }

    private Targetable pickFirstInRange(Tower tower, List<Targetable> targets,
            List<GridPosition> path) {
        Targetable best = null;
        for (Targetable target : targets) {
            if (!target.canBeTargeted()) {
                continue;
            }
            GridPosition tile = path.get((int) Math.floor(target.progress()));
            double dx = tile.x() - tower.x;
            double dy = tile.y() - tower.y;
            if (Math.sqrt(dx * dx + dy * dy) > RANGE) {
                continue;
            }
            if (best == null || target.progress() > best.progress()
                    || (target.progress() == best.progress() && target.id() < best.id())) {
                best = target;
            }
        }
        return best;
    }
}
