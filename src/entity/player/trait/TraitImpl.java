package entity.player.trait;

import entity.player.Player;
import entity.player.trait.reward.Reward;

import java.util.ArrayList;
import java.util.List;

public class TraitImpl implements Trait {

    private final String name;
    private final List<List<Reward>> rewards;
    private int level;

    public TraitImpl(String name, List<List<Reward>> rewards) {
        this(name, rewards, 0);
    }

    public TraitImpl(String name, List<List<Reward>> rewards, int startingLevel) {
        this.name = name;
        this.rewards = rewards;
        level = startingLevel;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int level() {
        return level;
    }

    @Override
    public List<Reward> nextRewards() {
        return rewards(level + 1);
    }

    @Override
    public List<Reward> rewards(int level) {
        return rewards.get(level);
    }

    @Override
    public List<Reward> earnedRewards() {
        List<Reward> out = new ArrayList<>();
        rewards.subList(0, level).forEach(out::addAll);
        return out;
    }

    @Override
    public void levelUp(Player p) {
        nextRewards().forEach(r -> r.apply(p));

        level++;
    }
}
