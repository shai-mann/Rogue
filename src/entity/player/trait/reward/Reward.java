package entity.player.trait.reward;

import entity.player.Player;
import entity.player.trait.Trait;

/**
 * A Reward is something given to the player on levelling up a {@link Trait}. Rewards can be
 * bonuses to stats, unlocking item types, spell types, etc.
 */
public interface Reward {

    /**
     * Applies the reward to the given {@link Player}.
     * @param p the Player to apply this reward to.
     */
    void apply(Player p);

    /**
     * Returns a string in plain english detailing what this reward is. Mainly helpful
     * for telling the player what it does.
     */
    String explain();

}
