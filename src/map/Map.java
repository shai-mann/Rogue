package map;

import com.sun.prism.paint.Color;
import entity.monster.Monster;
import extra.MessageBar;
import extra.StatusBar;
import helper.Helper;
import main.GameManager;
import map.level.Level;

import javax.swing.*;
import javax.swing.border.TitledBorder;

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
        GameManager.getPlayer().update();
        statusBar.updateStatusBar(); // must go after player update
    }

    // GETTER/ HELPER METHODS

    public static Map getMap() {
        return map;
    }
    private void createUIComponents() {
        level = new Level();
        statusBar = new StatusBar();
    }
    public void setDefaults() {
        panel.setBackground(Helper.BACKGROUND_COLOR);
        panel.setForeground(Helper.BACKGROUND_COLOR);
    }
}
