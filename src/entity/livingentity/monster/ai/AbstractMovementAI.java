package entity.livingentity.monster.ai;

import entity.livingentity.monster.Monster;

import java.util.Objects;

/**
 * Provides basic fields and default behaviors for {@link MovementAI} subclasses.
 */
public abstract class AbstractMovementAI implements MovementAI {

    protected boolean shouldBlockAttackAI = false;

    protected Monster self;

    public AbstractMovementAI(Monster self) {
        this.self = self;
    }

    public void setSource(Monster m) {
        this.self = m;
    }

    @Override
    public boolean move() {
        Objects.requireNonNull(self);
        shouldBlockAttackAI = false;

        return false;
    }

    @Override
    public boolean shouldTriggerSecondaryAI() {
        return false;
    }

    @Override
    public boolean blockAttackAITrigger() {
        return shouldBlockAttackAI;
    }

}
