package entity.livingentity.monster.ai;

import entity.livingentity.monster.Monster;

import java.util.Objects;

/**
 * Provides basic fields and default behaviors for {@link MovementAI} subclasses.
 */
public abstract class AbstractMovementAI implements MovementAI {

    protected boolean shouldTriggerSecondaryMovementAI = false;
    protected boolean shouldBlockAttackAI = true;

    protected Monster self;

    public AbstractMovementAI(Monster self) {
        this.self = self;
    }

    public void setSource(Monster m) {
        this.self = m;
    }

    @Override
    public void move() {
        Objects.requireNonNull(self);

        shouldTriggerSecondaryMovementAI = false;
        shouldBlockAttackAI = true;
    }

    @Override
    public boolean shouldTriggerSecondaryAI() {
        return shouldTriggerSecondaryMovementAI;
    }

    @Override
    public boolean blockAttackAITrigger() {
        return shouldBlockAttackAI;
    }

}
