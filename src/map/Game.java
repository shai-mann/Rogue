package map;

import main.GameManager;
import map.level.Level;
import state.StateManager;
import util.Helper;
import util.animation.Animation;
import util.animation.AnimationManager;
import util.gamepanes.MessageBar;
import util.gamepanes.StatusBar;
import util.gamepanes.saving.SavePane;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {

    private final StateManager stateManager = new StateManager();

    private StatusBar statusBar;
    private Level level;
    private JPanel panel;
    private MessageBar messageBar;
    private AnimationManager animationManager;

    public boolean saved = false;

    private static Game game;

    public Game() {
        setDefaults();

        GameManager.replaceContentPane(panel);
        game = this;
    }

    public Game(ArrayList<Animation> animations, String[] messages) {
        this();
        for (Animation a : animations) {
            animationManager.addAnimation(a);
        }
        messageBar.setMessages(messages);
    }

    public void update() {
        saved = false;
        MessageBar.nextTurn(); // must go first
        level.update();
        statusBar.updateStatusBar(); // must go after player update
        animationManager.update();
        level.render();
    }

    /* INITIALIZING HELPER METHODS */

    private void createUIComponents() {
        if (Level.getLevel() == null) {
            level = new Level();
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
        GameManager.getFrame().addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char s = '\u0013'; // TODO: ????????? change to encoded constant for god's sake
                if (e.isControlDown() && e.getKeyChar() == s) {
                    new SavePane();
                    GameManager.getFrame().setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                }
            }
        });
        animationManager = new AnimationManager();
    }

    private void addStateHooks() {
        level.addUpdateHooks(stateManager);

        stateManager.addHook(StateManager.Update.PLAYER_GUI, statusBar::updateStatusBar);
        stateManager.addHook(StateManager.Update.CHAT, MessageBar::nextTurn);
        stateManager.addHook(StateManager.Update.ANIMATIONS, animationManager::update);

        stateManager.addHook(level::render);
    }

    /* GETTER AND SETTER METHODS */

    public static Game getMap() {
        return game;
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
