package main;

import entity.livingentity.Player;
import entity.item.Item;
import entity.livingentity.Monster;
import extra.BeginPane;
import helper.Helper;
import map.level.table.CustomRoomTable;
import map.level.Level;

import javax.swing.*;

public class GameManager {

    private static JFrame frame;
    private static Player player;

    // TODO: Current bugs are:
    // Doorways can still be next to each other or inside of each other

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

    public static void runStaticSetupMethods() {
        Monster.loadCustomMonsters();
        Item.randomizeHiddenNames();
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
    public static void createPlayer() {
        player = new Player(Level.getLevel().getStartingRoom());
    }

}
