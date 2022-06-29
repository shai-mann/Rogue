package entity.player.trait.reward;

import entity.player.Player;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class StatReward implements Reward {

    private enum Type {
        ADD("+#"),
        PERCENT("#%");

        final String value;

        Type(String value) {
            this.value = value;
        }
    }

    private enum Trait {
        MAX_HEALTH("max health");

        final String value;

        Trait(String value) {
            this.value = value;
        }
    }

    private static final Map<Type, BiFunction<Number, Number, Integer>> typeMap = new HashMap<>();
    private static final Map<Trait, Pair<Function<Player, Integer>, BiConsumer<Player, Integer>>> traitMap = new HashMap<>();

    static {
        typeMap.put(Type.ADD, (num, delta) -> num.intValue() + delta.intValue());
        typeMap.put(Type.PERCENT, (num, delta) -> (int) (num.intValue() * delta.doubleValue()));

        traitMap.put(Trait.MAX_HEALTH, new Pair<>(Player::maxHealth, Player::setMaxHealth));
    }

    private final Trait trait;
    private final Type type;
    private final Number delta;

    private final BiFunction<Number, Number, Integer> calculateStat;
    private final Function<Player, Integer> getStat;
    private final BiConsumer<Player, Integer> setStat;

    public StatReward(String type, String trait, Number delta) {
        this.trait = Trait.valueOf(trait.toUpperCase());
        this.type = Type.valueOf(type.toUpperCase());
        this.delta = delta;

        calculateStat = typeMap.get(this.type);
        getStat = traitMap.get(this.trait).getKey();
        setStat = traitMap.get(this.trait).getValue();
    }

    @Override
    public void apply(Player p) {
        int curStatValue = getStat.apply(p);
        int adjustedStat = calculateStat.apply(curStatValue, delta);
        setStat.accept(p, adjustedStat);
    }

    @Override
    public String explain() {
        String formattedDelta = type.value.replace("#", String.valueOf(delta.doubleValue()));
        return "Increases " + trait.value + " by " + formattedDelta;
    }
}
