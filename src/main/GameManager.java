package main;

import entity.lifelessentity.item.Item;
import entity.livingentity.Player;
import entity.livingentity.monster.MonsterLoader;
import map.level.Level;
import map.level.table.CustomRoomTable;
import util.Helper;
import util.menupanes.BeginPane;

import javax.swing.*;

public class GameManager {

    private static JFrame frame;
    private static Player player;

    // TODO: used purely for testing purposes. When in development environment, true; when JAR, false.
    public static boolean notJAR = true;

    public static void main(String[] args) {
        initFrame();
        runStaticSetupMethods();

        new BeginPane();
    }

    // HELPER METHODS

    public static void replaceContentPane(JPanel panel) {
        frame.setContentPane(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.validate();
        frame.repaint();
    }
    public static void add(String s, int x, int y) {
        GameManager.getTable().getCustomModel().setValueAt(s, y, x);
    }

    // SETUP METHODS

    private static void runStaticSetupMethods() {
        MonsterLoader.loadMonsters();
        Item.randomizeHiddenNames();
    }
    private static void initFrame() {
        frame = new JFrame("Rogue - A commemoration of the 1980's game");

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setSize(Helper.getScreenSize());
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    // GETTER AND SETTER METHODS

    public static CustomRoomTable getTable() {
        return Level.getLevel().getTable();
    }
    public static JFrame getFrame() {
        return frame;
    }
    public static Player getPlayer() {
        return player;
    }
    public static void setPlayer(Player p) {
        player = p;
    }
    public static void createPlayer() {
        player = new Player(Level.getLevel().getStartingRoom());
    }

}
