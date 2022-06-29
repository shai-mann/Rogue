package map;

import main.GameManager;
import map.level.Level;
import state.StateManager;
import state.StateModel;
import util.Helper;
import util.animation.Animation;
import util.animation.AnimationManager;
import rendering.panels.gamepanes.MessageBar;
import rendering.panels.gamepanes.StatusBar;
import rendering.panels.gamepanes.saving.SavePane;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {

    private StatusBar statusBar;
    private Level level;
    private JPanel panel;
    private MessageBar messageBar;
    private AnimationManager animationManager;

    private final StateModel stateModel;
    private final StateManager stateManager;

    public boolean saved = false;

    private static Game game;

    public Game() {
        setDefaults();

        stateModel = new StateModel(
                level::getPlayer,
                () -> animationManager
        );
        stateManager = stateModel.getStateManager();

        addStateHooks();

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

        stateManager.addListener(level::render);
        stateManager.addListener(() -> saved = false);
    }

    /* GETTER AND SETTER METHODS */

    // todo: remove
    public static Game getMap() {
        return game;
    }

    public static StateModel stateModel() {
        return game.stateModel;
    }

    public MessageBar getMessageBar() {
        return messageBar;
    }

    public AnimationManager getAnimationManager() {
        return animationManager;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

}
