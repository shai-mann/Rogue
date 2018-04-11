package entity.item;

import helper.Helper;

public class Gold extends Item {
    private int amount;
    public Gold(int x, int y) {
        super("*", x, y);
        amount = Helper.random.nextInt(149) + 1; // TODO: max is 25 * level, max 500
    }
}
