package entity.component;

import java.io.Serializable;
import java.util.ArrayList;

public class Effect implements Serializable {

    // PROTECT_ARMOR is only for Player, and makes the player's armor impervious to rust attacks (reset when armor taken off)
    public static final int PROTECT_ARMOR = 0;
    // CONFUSE_ON_HIT is for both Monster and Player and makes the next hit confuse the entity it hits (not working yet)
    public static final int CONFUSE_ON_HIT = 1;
    // SUPPRESS_POWER is only for Monster, and makes the attackTypes and movementTypes of the data.monsters normal while the effect is on
    public static final int SUPPRESS_POWER = 2;
    // The following effects are only from Rings
    public static final int PROTECTION = 3;
    public static final int SEARCHING = 4;
    public static final int SEE_INVISIBLE = 5;
    public static final int SUSTAIN_STRENGTH = 6;
    public static final int STRENGTH = 7;
    public static final int REGENERATION = 8;
    public static final int AGGRAVATE_MONSTER = 9;
    public static final int SLOW_DIGESTION = 10;

    private final ArrayList<Integer> effects = new ArrayList<>();

    public Effect() {

    }
    public void addEffect(int type) {
        effects.add(type);
    }
    public void removeEffect(int type) {
        if (effects.contains(type)) {
            effects.remove(Integer.valueOf(type));
        }
    }
    public boolean hasEffect(int effect) {
        return effects.contains(effect);
    }
}
