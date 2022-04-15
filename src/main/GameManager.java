package main;

import entity.livingentity.Player;
import entity.lifelessentity.item.Item;
import entity.livingentity.Monster;
import util.menupanes.BeginPane;
import helper.Helper;
import map.level.table.CustomRoomTable;
import map.level.Level;

import javax.swing.*;
import java.awt.*;

public class GameManager {

    private static JFrame frame;
    private static Player player;

    public static boolean notJAR = true; // TODO: used purely for testing purposes. When in IntelliJ, true; when JAR, false.
    /*
        Testing environment should be extended as more testing-related features are implemented.
        For now, the testing environment adjusts the following:

        1) Level generation is only a single room.
     */
    public static boolean bootTestingEnvironment = true;

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
        Monster.loadCustomMonsters();
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
        return Level.getLevel().getHiddenTable();
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
