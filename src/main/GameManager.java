package main;

import entity.Player;
import entity.item.Item;
import entity.monster.Monster;
import helper.Helper;
import map.CustomRoomTable;
import map.Map;
import map.level.Level;

import javax.swing.*;

public class GameManager {

    private static JFrame frame;
    private static Map map;
    private static Player player;

    // TODO: Current bugs are:
    // Secret doors don't hide the doorway
    // Doorways can still be next to each other or inside of each other
    // Passageways sometimes still don't work

    public static void main(String[] args) {
        initFrame();

        map = new Map();
        player = new Player(Level.getLevel().getStartingRoom());

        runStaticSetupMethods();
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
        Monster.loadCustomMonsters();
        Item.randomizeHiddenNames();
        Monster.spawnMonsters();
    }
    private static void initFrame() {
        frame = new JFrame("Rogue - A recreation of the 1980's game");

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setSize(Helper.getScreenSize());
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    // GETTER METHODS

    public static CustomRoomTable getTable() {
        return Level.getLevel().getTable();
    }
    public static JFrame getFrame() {
        return frame;
    }
    public static Player getPlayer() {
        return player;
    }

}
