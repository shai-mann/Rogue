package entity.lifelessentity;

import entity.Entity;
import main.GameManager;

import java.util.ArrayList;

public class Trap extends Entity {

    /*
    * Traps have to be created last since they will look like a normal square, and is therefore a valid spawning space
    * When it shouldn't be.
    * When you step on a trap, it triggers the ability of the trap, doing something to the player.
     */

    private static ArrayList<Trap> traps = new ArrayList<>();
    private enum types {
        TELEPORT,
        ARROW,
        PARALYZE,
        WEAKEN
    }

    private boolean hidden = true;

    public Trap(int x, int y) {
        super("^", x, y);
        traps.add(this);

        GameManager.getTable().setValueAt(overWrittenGraphic, getYPos(), getXPos());
    }
    public boolean isHidden() {
        return hidden;
    }
    public void reveal() {
        hidden = false;

        GameManager.getTable().setValueAt("^", getYPos(), getXPos());
    }
    public void trigger() {
        // Use power
        reveal();
    }
    public static ArrayList<Trap> getTraps() {
        return traps;
    }
}
