package entity;

import java.util.ArrayList;

public class Effect {

    // PROTECT_ARMOR is only for Player, and makes the player's armor impervious to rust attacks (reset when armor taken off)
    public static final int PROTECT_ARMOR = 0;
    // CONFUSE_ON_HIT is for both Monster and Player and makes the next hit confuse the entity it hits (not working yet)
    public static final int CONFUSE_ON_HIT = 1;
    // SUPPRESS_POWER is only for Monster, and makes the attackTypes and movementTypes of the monsters normal while the effect is on
    public static final int SUPPRESS_POWER = 2;

    private ArrayList<Integer> effects = new ArrayList<>();

    public Effect() {

    }
    public void addEffect(int type) {
        effects.add(type);
    }
    public void removeEffect(int type) {
        if (effects.contains(type)) {
            effects.remove(type);
        }
    }
    public boolean hasEffect(int effect) {
        return effects.contains(effect);
    }
}
