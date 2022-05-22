package entity.lifeless.item;

import entity.lifeless.item.structure.AbstractItem;
import entity.lifeless.item.structure.Item;
import entity.structure.EntityProperties;
import map.level.Level;
import util.Helper;
import util.gamepanes.MessageBar;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Food extends AbstractItem implements Item {

    private static final List<String> NAMES = Arrays.asList(
        "Apples",
        "Nuts",
        "Meat",
        "Berries",
        "Dried fruit"
    );
    private static final String FOOD_GRAPHIC = ":";

    public Food(Point location) {
        super(new EntityProperties(Helper.getRandom(NAMES), FOOD_GRAPHIC, Color.GREEN), location);
    }

    @Override
    public boolean usable() {
        return true;
    }

    @Override
    public boolean use() {
        Level.getLevel().getPlayer().eat();
        Level.getLevel().getPlayer().getInventory().remove(this);
        MessageBar.addMessage("You eat the " + name());

        return true;
    }
}
