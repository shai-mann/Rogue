package map;

import entity.monster.Monster;
import helper.Helper;
import main.GameManager;

import javax.swing.*;

public class StatusBar extends JComponent {
    private JLabel healthField;
    private JPanel panel;

    // This class is what takes the player's stats and displays them at the bottom of the screen
    public StatusBar() {

        setDefaults();
    }
    public void setDefaults() {
        panel.setForeground(Helper.BACKGROUND_COLOR);
        panel.setBackground(Helper.BACKGROUND_COLOR);

        healthField.setText("Health: " + Monster.DEFAULT_HEALTH);
    }
    public void updateStatusBar() {
        healthField.setText("Health: " + GameManager.getPlayer().getHealth());
        panel.revalidate();
        panel.repaint();
    }
}
