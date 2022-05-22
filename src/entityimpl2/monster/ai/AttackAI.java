package entityimpl2.monster.ai;

import entityimpl2.monster.Monster;
import entityimpl2.player.Player;

/**
 * The AttackAI interface describes how monsters attempt to attack. The AI handles two main components of
 * monster behavior:
 *
 * 1) Determining if an attack is possible (in range of player - determined by type of attack)
 * 2) Performing attacks on the {@link Player}
 *
 * This AI can be prevented from being triggered by outside elements such as the {@link MovementAI},
 * but those checks are expected to be performed within the {@link Monster} class.
 */
public interface AttackAI {

    /**
     * Used to indicate the outcome of an attempted attack.
     *  - Unperformed: no attack was performed (generally not in range of a {@link Player}).
     *  - Success: successfully performed the attack.
     *  - Fail: Swung at the player but missed.
     *  - Crit: Dealt a critical hit to the player.
     */
    enum Outcome {
        UNPERFORMED,
        SUCCESS,
        FAIL,
        CRIT,
        BLOCKED
    }

    /**
     * Handles the attacking sequence of a monster (checks that an attack is possible,
     * then performs the attack).
     * @return the {@link Outcome} of the attack.
     */
    Outcome attack();

}
