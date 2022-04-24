package map;

import entity.livingentity.monster.Monster;
import util.animation.Animation;
import util.gamepanes.MessageBar;
import util.gamepanes.saving.SavePane;
import util.gamepanes.StatusBar;
import util.Helper;
import main.GameManager;
import map.level.Level;
import util.animation.AnimationManager;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.util.ArrayList;

public class Map implements Serializable {
    private StatusBar statusBar;
    private Level level;
    private JPanel panel;
    private MessageBar messageBar;
    private AnimationManager animationManager;

    public boolean saved = false;

    private static Map map;

    public Map() {
        setDefaults();

        GameManager.replaceContentPane(panel);
        map = this;
    }

    public Map(ArrayList<Animation> animations, String[] messages) {
        this();
        for (Animation a : animations) {
            animationManager.addAnimation(a);
        }
        messageBar.setMessages(messages);
    }

    public void update() {
        saved = false;
        MessageBar.nextTurn(); // must go first
        Monster.update();
        GameManager.getPlayer().update();
        statusBar.updateStatusBar(); // must go after player update
        animationManager.update();
        level.render();
    }

    // GETTER/HELPER METHODS

    public static Map getMap() {
        return map;
    }
    private void createUIComponents() {
        if (Level.getLevel() == null) {
            level = GameManager.bootTestingEnvironment ? new Level(true) : new Level();
        } else {
            level = Level.getLevel();
        }
        statusBar = new StatusBar();
    }
    private void setDefaults() {
        panel.setBackground(Helper.BACKGROUND_COLOR);
        panel.setForeground(Helper.BACKGROUND_COLOR);

        GameManager.getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        GameManager.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (!saved) {
                    new SavePane();
                    GameManager.getFrame().setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                } else {
                    super.windowClosing(e);
                    GameManager.getFrame().dispose();
                    System.exit(0);
                }
            }
        });
        GameManager.getFrame().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char s = '\u0013'; // TODO: ????????? change to encoded constant for god's sake
                if (e.isControlDown() && e.getKeyChar() == s) {
                    new SavePane();
                    GameManager.getFrame().setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                }
            }
            @Override
            public void keyPressed(KeyEvent e) {

            }
            @Override
            public void keyReleased(KeyEvent e) {

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
    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}
