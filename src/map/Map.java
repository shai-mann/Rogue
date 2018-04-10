package map;

import entity.Entity;
import entity.monster.Monster;
import helper.Helper;
import main.GameManager;
import map.level.Level;

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
    private void createUIComponents() {
        level = new Level();
    }
    public void setDefaults() {
        panel.setBackground(Helper.BACKGROUND_COLOR);
        panel.setForeground(Helper.BACKGROUND_COLOR);
    }
    public Level getLevel() {
        return level;
    }
}
