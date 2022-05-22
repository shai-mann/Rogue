package entityimpl2.lifeless.item.structure;

import entityimpl2.player.Player;
import entityimpl2.structure.AbstractEntity;
import entityimpl2.structure.EntityProperties;

import java.awt.*;

public abstract class AbstractItem extends AbstractEntity implements Item {

    private boolean collected = false;

    public AbstractItem(EntityProperties properties, Point location) {
        super(properties, location);
    }

    @Override
    public boolean usable() {
        return false;
    }

    // default behavior
    @Override
    public boolean use() {
        return false;
    }

    @Override
    public boolean isCollected() {
        return collected;
    }

    @Override
    public void pickup(Player p) {
        collected = true;
    }
}
