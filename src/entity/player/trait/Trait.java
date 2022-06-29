package entity.player.trait;

import entity.player.Player;
import entity.player.trait.reward.Reward;

import java.util.List;

public interface Trait {

    /**
     * @return the name of this Trait.
     */
    String name();

    /**
     * @return the current level of this Trait.
     */
    int level();

    /**
     * @return the list of {@link Reward} to be given on the next level up of this Trait.
     */
    List<Reward> nextRewards();

    /**
     * @param level the level of this trait to find the {@link Reward}s for.
     * @return the {@link Reward} associated with levelling up this Trait to the given level.
     */
    List<Reward> rewards(int level);

    /**
     * @return all {@link Reward}s this {@link Player} has earned.
     */
    List<Reward> earnedRewards();

    /**
     * Levels up this Trait, applying the new {@link Reward}s to the given {@link Player}.
     * @param p the {@link Player} levelling up this Trait.
     */
    void levelUp(Player p);

}
