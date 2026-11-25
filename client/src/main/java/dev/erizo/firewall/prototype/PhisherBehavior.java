package dev.erizo.firewall.prototype;

/**
 * Phisher stats; it walks disguised and cannot be targeted until revealed.
 */
final class PhisherBehavior implements EnemyBehavior {

    @Override
    public String type() {
        return "PHISHER";
    }

    @Override
    public void configure(Enemy enemy) {
        enemy.integrity = 8;
        enemy.speed = 1.2;
        enemy.leak = 2;
        enemy.bounty = 12;
        enemy.disguised = true;
    }
}
