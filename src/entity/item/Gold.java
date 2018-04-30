package entity.item;

import helper.Helper;

public class Gold extends Item {

    private int amount = 0;

    public Gold(int x, int y) {
        super("*", x, y);
        amount = Helper.random.nextInt(149) + 1; // TODO: max is 25 * level, max 500
    }
    public int getAmount() {
        return amount;
    }
}
