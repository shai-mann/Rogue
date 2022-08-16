package entity.lifeless.item;

import entity.lifeless.item.structure.AbstractItem;
import entity.lifeless.item.structure.Item;
import entity.structure.EntityProperties;
import map.Game;
import util.Helper;

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
        Game.stateModel().getPlayer().eat();
        Game.stateModel().getPlayer().getInventory().remove(this);
        Game.stateModel().message("You eat the " + name());

        return true;
    }
}
