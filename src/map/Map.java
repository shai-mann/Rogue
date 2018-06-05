package map;

import com.sun.org.apache.xpath.internal.operations.Bool;
import entity.livingentity.Monster;
import util.MessageBar;
import util.SavePane;
import util.StatusBar;
import helper.Helper;
import main.GameManager;
import map.level.Level;
import util.animation.AnimationManager;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Map {
    private StatusBar statusBar;
    private Level level;
    private JPanel panel;
    private MessageBar messageBar;
    private AnimationManager animationManager;

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
        animationManager.update();
        level.update();
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

        GameManager.getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        GameManager.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new SavePane();
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        animationManager = new AnimationManager();
    }
    public MessageBar getMessageBar() {
        return messageBar;
    }
    public StatusBar getStatusBar() {
        return statusBar;
    }
    public AnimationManager getAnimationManager() {
        return animationManager;
    }
}
