package main;

import entity.Player;
import entity.item.Armor;
import entity.item.Gold;
import entity.item.Item;
import entity.monster.Monster;
import helper.Helper;
import map.Map;

import javax.swing.*;
import java.io.File;

public class GameManager {

    static JFrame frame;
    static Map map;
    static Player player;

    public static void main(String[] args) {
        initFrame();

        map = new Map();
        player = new Player();

        loadCustomMonsters();

        Item.randomizeHiddenNames();
    }
    private static void loadCustomMonsters() {
        // TODO: Move this method to Level Class once generation is finished
        File[] files = new File("data/monsters/").listFiles();
        for (File file : files) {
            if (file.isFile()) {
                    new Monster(file.getPath(), Helper.random.nextInt(28) + 1, Helper.random.nextInt(28) + 1);
            }
        }
        new Gold(Helper.random.nextInt(28) + 1, Helper.random.nextInt(28) + 1);
//        new Armor(Helper.random.nextInt(28) + 1, Helper.random.nextInt(28) + 1);
        new Armor(2, 2);
    }
    public static void replaceContentPane(JPanel panel) {
        frame.setContentPane(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.validate();
        frame.repaint();
    }
    public static void add(String s, int x, int y) {
        map.getRoom(player).add(s, x, y);
    }
    public static void initFrame() {
        frame = new JFrame("Rogue");

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setSize(Helper.getScreenSize());
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    public static JTable getTable() {
        return map.getRoom(player).getTable();
    }
    public static JFrame getFrame() {
        return frame;
    }

    public static Player getPlayer() {
        return player;
    }

    public static String getValueAt(int x, int y) {
        return map.getRoom(player).getValueAt(x, y);
    }
}
