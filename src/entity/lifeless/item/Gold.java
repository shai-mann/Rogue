package entity.lifeless.item;

import entity.lifeless.item.structure.AbstractItem;
import entity.lifeless.item.structure.Item;
import entity.player.Player;
import entity.structure.EntityProperties;

import java.awt.*;

public class Gold extends AbstractItem implements Item {

    public static final String GOLD_NAME = "gold", GOLD_GRAPHIC = "*";

    private static final EntityProperties GOLD_PROPERTIES = new EntityProperties(
            GOLD_NAME, GOLD_GRAPHIC, Color.GREEN
    );

    private final int amount;

    public Gold(int amount, Point location) {
        super(GOLD_PROPERTIES, location);

        this.amount = amount;
    }

    @Override
    public void pickup(Player p) {
        p.changeGold(amount);
        super.pickup(p);
    }
}
