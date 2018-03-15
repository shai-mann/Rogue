package map;

import entity.Entity;
import entity.monster.Monster;
import main.GameManager;

import javax.swing.*;

public class Map {
    private StatusBar statusBar;
    private Room room;
    private JPanel panel;

    private static Map map;

    public Map() {
        GameManager.replaceContentPane(panel);

        map = this;
    }
    public void update() {
        Monster.update();
        statusBar.updateStatusBar();
    }
    public static Map getMap() {
        return map;
    }
    public Room getRoom(Entity entity) {
        // This method will return the room which the entity passed is in. *NOT FUNCTIONAL*
        return room;
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        room = new Room(10, 10);
    }
}
