package main;

import entity.Player;
import entity.monster.Zombie;
import helper.Helper;
import map.Room;

import javax.swing.*;

public class GameManager {

    static JFrame frame;
    static Room room;
    static Player player;

    public static void main(String[] args) {
        initFrame();

        room = new Room(10, 10);
        player = new Player();
        new Zombie(2, 2);
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
        room.add(s, x, y);
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
        return room.getTable();
    }
    public static JFrame getFrame() {
        return frame;
    }

    public static Player getPlayer() {
        return player;
    }

    public static String getValueAt(int x, int y) {
        return room.getValueAt(x, y);
    }
}
