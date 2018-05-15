package entity;

import java.util.ArrayList;

public class Effect {

    public final static int PROTECT_ARMOR = 0;
    public static final int CONFUSE_ON_HIT = 1;

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
