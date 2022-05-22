package entityimpl2.lifeless.item.combat;

import entityimpl2.lifeless.item.structure.AbstractItem;
import entityimpl2.lifeless.item.structure.Item;
import entityimpl2.structure.EntityProperties;

import java.awt.*;

public class Arrow extends AbstractItem implements Item {

    private static final String ARROW_NAME = "arrows", ARROW_GRAPHIC = ")";
    private static final EntityProperties ARROW_PROPERTIES = new EntityProperties(
        ARROW_NAME, ARROW_GRAPHIC, Color.GREEN
    );

    private final int amount;

    public Arrow(int amount, Point location) {
        super(ARROW_PROPERTIES, location);
        this.amount = amount;
    }

    public int amount() {
        return amount;
    }
}
