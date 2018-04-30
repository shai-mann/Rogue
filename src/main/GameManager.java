package main;

import entity.Player;
import entity.item.Armor;
import entity.item.Gold;
import entity.item.Item;
import entity.monster.Monster;
import helper.Helper;
import map.CustomRoomTable;
import map.Map;
import map.level.Level;

import javax.swing.*;
import java.io.File;

public class GameManager {

    private static JFrame frame;
    private static Map map;
    private static Player player;

    // TODO: Current bugs are:
    // Sometimes the passageways generate strangely, failing to get to the room they are trying to get to
    // Secret doors don't hide the doorway
    // Doorways can still be next to each other or inside of each other
    // Player has chance to spawn outside of the map or in the wall of a room and generates in two places

    public static void main(String[] args) {
        initFrame();

        map = new Map();
<<<<<<< HEAD
        player = new Player(Level.getLevel().getStartingRoom());
//        loadCustomMonsters();
=======
        player = new Player();

        loadCustomMonsters();

        Item.randomizeHiddenNames();
>>>>>>> items
    }
    private static void loadCustomMonsters() {
        // TODO: Move this method to Level Class once generation is finished
        File[] files = new File("data/monsters/").listFiles();
        for (File file : files) {
            if (file.isFile()) {
                    new Monster(file.getPath(), Helper.random.nextInt(28) + 1, Helper.random.nextInt(28) + 1);
            }
        }
<<<<<<< HEAD
        player = new Player(Level.getLevel().getStartingRoom());
=======
        new Gold(Helper.random.nextInt(28) + 1, Helper.random.nextInt(28) + 1);
//        new Armor(Helper.random.nextInt(28) + 1, Helper.random.nextInt(28) + 1);
        new Armor(2, 2);
        new Armor(2, 1);
>>>>>>> items
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
    public static void initFrame() {
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
