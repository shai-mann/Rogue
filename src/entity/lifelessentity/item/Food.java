package entity.lifelessentity.item;

import util.gamepanes.MessageBar;
import util.Helper;
import main.GameManager;

import java.util.ArrayList;
import java.util.Arrays;

public class Food extends Item {

    private int amount;

    private static String[] names = {
            "Apples",
            "Nuts",
            "Meat",
            "Berries",
            "Dried fruit"
    };

    public Food(int x, int y) {
        super(":", x, y);

        name = (String) Helper.getRandom(new ArrayList(Arrays.asList(names)));
        amount = Helper.random.nextInt(15) + 5;
    }
    public Food(Food food) {
        super(":", GameManager.getPlayer().getXPos(), GameManager.getPlayer().getYPos());
        overWrittenGraphic = "-";
        this.name = food.getName();
        this.amount = food.amount;
    }
    @Override
    public void use() {
        GameManager.getPlayer().eat();

        MessageBar.addMessage("You eat the " + getName());
        GameManager.getPlayer().getInventory().remove(this);
    }
}
