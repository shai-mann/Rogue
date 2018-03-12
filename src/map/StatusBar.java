package map;

import entity.Player;
import helper.Helper;

import javax.swing.*;

public class StatusBar extends JComponent {
    private static JLabel healthField;
    private JPanel panel;

    private static Player player;

    // This class is what takes the player's stats and displays them at the bottom of the screen
    public StatusBar(Player entity) {
        player = entity;

        setDefaults();
    }
    public void setDefaults() {
        panel.setForeground(Helper.BACKGROUND_COLOR);
        panel.setBackground(Helper.BACKGROUND_COLOR);

        healthField.setText("Health: " + player.getHealth());
    }
    public static void updateStatusBar() {
        healthField.setText("Health: " + player.getHealth());
    }
}
