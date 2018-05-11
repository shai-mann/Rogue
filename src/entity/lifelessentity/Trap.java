package entity.lifelessentity;

import entity.Entity;
import main.GameManager;

import java.util.ArrayList;

public class Trap extends Entity {

    private static ArrayList<Trap> traps = new ArrayList<>();

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
    public static ArrayList<Trap> getTraps() {
        return traps;
    }
}
