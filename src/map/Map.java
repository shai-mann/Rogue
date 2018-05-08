package map;

import entity.livingentity.Monster;
import entity.livingentity.Player;
import extra.MessageBar;
import extra.SavePane;
import extra.StatusBar;
import helper.Helper;
import main.GameManager;
import map.level.Level;
import oracle.jrockit.jfr.JFR;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

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

    // GETTER/HELPER METHODS

    public static Map getMap() {
        return map;
    }
    private void createUIComponents() {
        level = new Level();
        statusBar = new StatusBar();
    }
    private void setDefaults() {
        panel.setBackground(Helper.BACKGROUND_COLOR);
        panel.setForeground(Helper.BACKGROUND_COLOR);

        GameManager.getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        GameManager.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new SavePane((JPanel) GameManager.getFrame().getContentPane());
            }
        });
    }
    public MessageBar getMessageBar() {
        return messageBar;
    }
    public StatusBar getStatusBar() {
        return statusBar;
    }
}
