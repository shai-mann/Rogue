package entityimpl2.lifeless.item;

import entityimpl2.lifeless.item.structure.AbstractItem;
import entityimpl2.lifeless.item.structure.Item;
import entityimpl2.player.Player;
import entityimpl2.structure.EntityProperties;

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
