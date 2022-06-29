package entity.lifeless.item.structure;

import entity.player.Player;
import entity.structure.Entity;
import rendering.structure.Renderer;
import rendering.panels.gamepanes.MessageBar;

public interface Item extends Entity, Renderer {

    boolean usable();

    /**
     * Uses the item. Implementation is specific to the implementing classes.
     * @return true if the item should be removed from the players inventory, false otherwise.
     */
    boolean use();

    boolean isCollected();

    void pickup(Player p);

    default void identify() {
        MessageBar.addMessage("You identified a " + name());
    }

}
