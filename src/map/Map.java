package map;

import entity.monster.Monster;
import extra.MessageBar;
import extra.StatusBar;
import helper.Helper;
import main.GameManager;
import map.level.Level;

import javax.swing.*;

public class Map {
    private StatusBar statusBar;
    private Level level;
    private JPanel panel;
    private MessageBar messageBar;

    private static Map map;

    public Map() {
        setDefaults();
        GameManager.replaceContentPane(panel);
        map = this;
    }
    public void update() {
        MessageBar.nextTurn(); // must go first
        Monster.update();
        statusBar.updateStatusBar();
        GameManager.getPlayer().update();
    }

    // GETTER/ HELPER METHODS

    public static Map getMap() {
        return map;
    }
    public Level getRoom(Entity entity) {
        // This method will return the level which
        // the entity passed is in. *NOT FUNCTIONAL*
        return level;
    }
    private void createUIComponents() {
        level = new Level();
    }
    public void setDefaults() {
        panel.setBackground(Helper.BACKGROUND_COLOR);
        panel.setForeground(Helper.BACKGROUND_COLOR);
    }
}
