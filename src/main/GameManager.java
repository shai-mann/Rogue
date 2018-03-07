package main;

import entity.Player;
import helper.Helper;
import room.Room;

import javax.swing.*;

public class GameManager {

    static JFrame frame;
    static Room room;
    static Player player;

    public static void main(String[] args) {
        initFrame();

        room = new Room(10, 10);
        player = new Player();
    }
    public static void replaceContentPane(JPanel panel) {
        frame.setContentPane(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.validate();
        frame.repaint();
    }
    public static void add(char s, int x, int y) {
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

    public static JFrame getFrame() {
        return frame;
    }
}
