package main;

import entityimpl2.lifeless.item.Potion;
import entityimpl2.lifeless.item.Ring;
import entityimpl2.lifeless.item.Scroll;
import entityimpl2.lifeless.item.Wand;
import entityimpl2.monster.MonsterLoader;
import util.Helper;
import util.menupanes.BeginPane;

import javax.swing.*;

public class GameManager {

    private static JFrame frame;

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

    // SETUP METHODS

    // todo: move to Map?
    private static void runStaticSetupMethods() {
        MonsterLoader.loadMonsters();
        Ring.obfuscate();
        Wand.obfuscate();
        Potion.obfuscate();
        Scroll.obfuscate();
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

    public static JFrame getFrame() {
        return frame;
    }

}
