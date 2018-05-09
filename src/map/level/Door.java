package map.level;

import extra.MessageBar;
import main.GameManager;

import java.awt.*;
import java.util.ArrayList;

public class Door extends Point {

    private boolean secret = false;
    private String overWrittenGraphic;

    private static ArrayList<Door> doors = new ArrayList<>();

    public Door(Point p, boolean secret, String overWrittenGraphic) {
        super(p);
        this.secret = secret;
        this.overWrittenGraphic = overWrittenGraphic;
        doors.add(this);
        if (isSecret()) {
            GameManager.getTable().setValueAt(overWrittenGraphic, p.y, p.x);
        }
        // TODO: Figure out why secret doors aren't working
    }

    // GETTER METHODS

    public boolean isSecret() {
        return secret;
    }
    public void reveal() {
        secret = true;
        GameManager.getTable().setValueAt("+", y, x);
        GameManager.getFrame().repaint();

        MessageBar.addMessage("You have discovered a secret door");
    }
    public static ArrayList<Door> getDoors() {
        return doors;
    }
}
