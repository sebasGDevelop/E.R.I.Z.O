package dev.erizo.firewall.defense;

import java.util.List;

/** A tower behavior that acts once each time its cooldown elapses. */
abstract class CooldownBehavior implements TowerBehavior {

    @Override
    public final void act(Tower tower, List<Enemy> enemiesInRange, EnemyPath path) {
        if (!tower.isReady()) {
            tower.tickCooldown();
            return;
        }
        boolean firedAny = false;
        for (int shot = 0; shot < tower.shotsPerCooldown(); shot++) {
            firedAny = fire(tower, enemiesInRange, path) || firedAny;
        }
        if (firedAny) {
            tower.startCooldown();
        }
    }

    /** Performs the attack and reports whether it fired, resetting the cooldown. */
    protected abstract boolean fire(Tower tower, List<Enemy> enemiesInRange, EnemyPath path);
}
