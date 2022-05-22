package entityimpl2.util;

import entityimpl2.structure.LivingEntity;

/**
 * A MoveResult is either a boolean, or an {@link LivingEntity}. True if the move
 * given can be executed, false if it would collide with a non-{@link entity.livingentity.monster.Monster}
 * obstruction, and the Entity it would collide with if it would collide with an Entity.
 */
public class MoveResult {
    public static MoveResult TRUE = new MoveResult(true), FALSE = new MoveResult(false);

    private LivingEntity collisionEntity = null;
    private boolean validMove = false;

    public MoveResult(boolean validMove) {
        this.validMove = validMove;
    }

    public MoveResult(LivingEntity collisionEntity) {
        this.collisionEntity = collisionEntity;
    }

    public boolean takesAction() {
        return hitsEntity() || isValidMove();
    }

    public boolean hitsEntity() {
        return collisionEntity != null;
    }

    public boolean isValidMove() {
        return !hitsEntity() && validMove;
    }

    public LivingEntity getCollisionEntity() {
        return collisionEntity;
    }
}
