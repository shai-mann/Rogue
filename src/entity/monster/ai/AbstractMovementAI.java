package entity.monster.ai;

import entity.monster.Monster;
import entity.util.MoveResult;
import map.level.Level;

import java.awt.*;

/**
 * Provides basic fields and default behaviors for {@link MovementAI} subclasses.
 */
public abstract class AbstractMovementAI implements MovementAI {

    protected boolean shouldBlockAttackAI = false;
    protected Monster self;

    public AbstractMovementAI(Monster self) {
        this.self = self;
    }

    /**
     * Called during overridden move methods before making any actual moves
     * @param displacement the displacement this entity is attempting to move by.
     * @return a {@link MoveResult} representing the result of the move
     * @see MoveResult
     */
    protected MoveResult attemptMove(Point displacement) {
        MoveResult result = Level.getLevel().isValidMove(self, displacement);

        if (!result.isValidMove()) return result;

        self.move(displacement);

        return result;
    }

    @Override
    public MoveResult move() {
        shouldBlockAttackAI = false; // todo: reevalute if this system is used

        return MoveResult.FALSE;
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
