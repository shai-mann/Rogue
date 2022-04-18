package entity.lifelessentity.item.combat;

import entity.lifelessentity.item.Item;
import util.Helper;
import main.GameManager;

public class Arrow extends Item {

    private int amount = 1;

    public Arrow(int x, int y) {
        super(")", x, y);
        amount = Helper.random.nextInt(20) + 5;
        name = String.valueOf(amount) + " arrows";
    }
    public Arrow(Arrow arrow) {
        super(")", GameManager.getPlayer().getXPos(), GameManager.getPlayer().getYPos());
        overWrittenGraphic = "-";
        amount = arrow.getAmount();
    }
    public int getAmount() {
        return amount;
    }
}
