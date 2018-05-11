package entity;

import java.util.ArrayList;

public class Effect {

    public final static int PROTECT_ARMOR = 0;
    public static final int CONFUSE_ON_HIT = 1;

    private ArrayList<Integer> types = new ArrayList<>();

    public Effect() {

    }
    public void addEffect(int type) {
        types.add(type);
    }
    public void update() {
        for (int i: types) {
            switch (i) {
                case PROTECT_ARMOR:
                    break;
                case CONFUSE_ON_HIT:
                    break;
            }
        }
    }
}
