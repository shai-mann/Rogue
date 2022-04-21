package entity.lifelessentity.item;

import util.Helper;
import main.GameManager;

public class Gold extends Item {

    private int amount = 0;

    public Gold(int x, int y) {
        super("*", x, y);
        int multiplier = GameManager.getPlayer() != null ? GameManager.getPlayer().getLevel() * 25 : 1;
        multiplier = multiplier > 500 ? 500 : multiplier;
        amount = Helper.random.nextInt(multiplier) + 80;
    }
    public Gold(int x, int y, int amount) {
        this(x, y);
        this.amount = amount;
    }
    public int getAmount() {
        return amount;
    }
}
