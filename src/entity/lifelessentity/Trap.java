package entity.lifelessentity;

import entity.Entity;
import util.Helper;
import main.GameManager;
import map.level.Room;
import util.gamepanes.MessageBar;

import java.util.ArrayList;
import java.util.Arrays;

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
        WEAKNESS
    }

    private types type;
    private boolean hidden = true;

    public Trap(int x, int y) {
        super("^", x, y);
        type = (types) Helper.getRandom(new ArrayList(Arrays.asList(types.values())));
        name = type.toString().toLowerCase().concat(" trap");
        type = types.TELEPORT;
        traps.add(this);

        GameManager.getTable().setValueAt(overWrittenGraphic, getYPos(), getXPos());
    }
    public boolean isHidden() {
        return hidden;
    }
    public void reveal() {
        hidden = false;

        overWrittenGraphic = GameManager.getPlayer().overWrittenGraphic;
    }
    public void trigger() {
        reveal();
        // Use power
        switch (type) {
            case ARROW:
                GameManager.getPlayer().getStatus().lowerHealth(Helper.random.nextInt(2) + 5);
                MessageBar.addMessage("An arrow hits your shoulder");
                break;
            case WEAKNESS:
                GameManager.getPlayer().getStatus().setWeakened(Helper.random.nextInt(10) + 8);
                break;
            case PARALYZE:
                GameManager.getPlayer().getStatus().setParalyzed(Helper.random.nextInt(5) + 5);
                break;
            case TELEPORT:
                GameManager.getPlayer().setLocation((Room) Helper.getRandom(Room.rooms));
                GameManager.getPlayer().setOverwrittenGraphic("-");
                GameManager.getTable().setValueAt("^", getYPos(), getXPos());
                break;
        }
        MessageBar.addMessage("You activated a " + getName());
    }
    public static ArrayList<Trap> getTraps() {
        return traps;
    }
}
