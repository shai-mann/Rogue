package entity.livingentity.monster.ai;

/**
 * The AttackAI interface describes how monsters attempt to attack. The AI handles two main components of
 * monster behavior:
 *
 * 1) Determining if an attack is possible (in range of player - determined by type of attack)
 * 2) Performing attacks on the {@link entity.livingentity.Player}
 *
 * This AI can be prevented from being triggered by outside elements such as the {@link MovementAI},
 * but those checks are expected to be performed within the {@link entity.livingentity.monster.Monster} class.
 */
public interface AttackAI {



}
