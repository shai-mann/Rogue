package entity.lifeless.item.structure;

import entity.player.Player;
import entity.structure.AbstractEntity;
import entity.structure.EntityProperties;

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
