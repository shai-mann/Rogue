package map;

import entity.Entity;
import entity.monster.Monster;
import helper.Helper;
import main.GameManager;

import javax.swing.*;
import java.awt.*;

public class Map {
    private StatusBar statusBar;
    private Room room;
    private JPanel panel;

    private static Map map;

    public Map() {
        setDefaults();
        GameManager.replaceContentPane(panel);

        map = this;
    }
    public void update() {
        Monster.update();
        statusBar.updateStatusBar();
        GameManager.getPlayer().update();
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
        room = new Room(30, 30);
    }
    public void setDefaults() {
        panel.setBackground(Helper.BACKGROUND_COLOR);
        panel.setForeground(Helper.BACKGROUND_COLOR);
    }
}
