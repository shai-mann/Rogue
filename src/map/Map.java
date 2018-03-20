package map;

import entity.Entity;
import entity.monster.Monster;
import helper.Helper;
import main.GameManager;

import javax.swing.*;

public class Map {
    private StatusBar statusBar;
    private Level level;
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
    public Level getRoom(Entity entity) {
        // This method will return the level which the entity passed is in. *NOT FUNCTIONAL*
        return level;
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        level = new Level(30, 30);
    }
    public void setDefaults() {
        panel.setBackground(Helper.BACKGROUND_COLOR);
        panel.setForeground(Helper.BACKGROUND_COLOR);
    }
}
