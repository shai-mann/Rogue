package entity.livingentity.monster.ai;

import entity.livingentity.monster.Monster;

import java.util.Optional;

/**
 * The MovementAI interface is used to describe the ways that a monster would move.
 * There are a couple different movement types:
 *
 * 1) Track - follows the player, triggers AttackAI if possible
 * 2) Wander - moves randomly, triggers AttackAI if attempts to move into player
 * 3) Still - stays still, can trigger the AttackAI
 * 4) Sleep - stays still, switches to secondary movement AI once attacked
 * 5) Mimic - disguises self as treasure (overrides graphic), triggers AttackAI if player in range
 *
 * The AI is assumed to only be called if it is able to move, and therefore has no checks on
 * whether it is frozen or paralyzed or otherwise incapable of moving.
 *
 * It will ultimately reference the {@link Monster} 'move' method, which will handle triggering
 * the {@link AttackAI} linked to it if necessary (or invalidating the move if something is in the way).
 *
 * In the instance where the monster attempts to move into a wall or is obstructed in one way or another,
 * the {@link Monster} class should attempt to call the move method again.
 */
public interface MovementAI {

    /**
     * Attempts to make a move. Returns true if the movement was successful, false otherwise.
     * @return true if the move occurred, false otherwise.
     */
    boolean move();

    /**
     * Used by {@link entity.livingentity.monster.MonsterAttributes} to determine if the secondary AI should be used
     * instead of the main movementAI. The secondary AI defaults as
     * {@link entity.livingentity.monster.ai.movement.WanderAI} in the
     * {@link entity.livingentity.monster.MonsterClassBuilder}.
     * @return true if the secondary AI should be triggered.
     */
    boolean shouldTriggerSecondaryAI();

    /**
     * Used by {@link Monster} to confirm that an {@link AttackAI} trigger is not prevented by the
     * MovementAI it uses.
     * @return true if an {@link AttackAI} cannot be triggered, false otherwise.
     */
    boolean blockAttackAITrigger();

    // TODO: actually implement?
    default Optional<String> hasOverriddenGraphic() {
        return Optional.empty();
    }

}
