package map.level;

import util.MessageBar;
import main.GameManager;

import java.awt.*;
import java.util.ArrayList;

public class Door extends Point {

    private boolean secret = false;

    private static ArrayList<Door> doors = new ArrayList<>();

    public Door(Point p, boolean secret, String overWrittenGraphic) {
        super(p);
        this.secret = secret;
        doors.add(this);
        if (isSecret()) {
            GameManager.getTable().setValueAt(overWrittenGraphic, p.y, p.x);
        }
    }

    // GETTER METHODS

    public boolean isSecret() {
        return secret;
    }
    public void reveal() {
        secret = false;
        GameManager.getTable().setValueAt("+", y, x);

        MessageBar.addMessage("You have discovered a secret door");
    }
    public static ArrayList<Door> getDoors() {
        return doors;
    }
}
