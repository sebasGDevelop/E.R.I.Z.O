package dev.erizo.firewall.defense;

import java.util.function.Supplier;

/** The selectable targeting strategies, each able to create its strategy. */
public enum TargetStrategyKind {

    FIRST(FirstTarget::new),
    STRONGEST(StrongestTarget::new),
    CLOSEST(ClosestTarget::new);

    private final Supplier<TargetStrategy> factory;

    TargetStrategyKind(Supplier<TargetStrategy> factory) {
        this.factory = factory;
    }

    /** Creates a fresh instance of this targeting strategy. */
    public TargetStrategy create() {
        return factory.get();
    }
}
