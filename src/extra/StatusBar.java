package extra;

import entity.monster.Monster;
import helper.Helper;
import main.GameManager;

import javax.swing.*;

public class StatusBar extends JComponent {
    private JLabel healthField;
    private JPanel panel;
    private JLabel goldField;

    // This class is what takes the player's stats and displays them at the bottom of the screen
    public StatusBar() {

        setDefaults();
    }
    public void setDefaults() {
        panel.setForeground(Helper.BACKGROUND_COLOR);
        panel.setBackground(Helper.BACKGROUND_COLOR);

        healthField.setText("Health: " + Monster.DEFAULT_HEALTH);
        healthField.setForeground(Helper.FOREGROUND_COLOR);
        goldField.setText("Gold: 0");
        goldField.setForeground(Helper.FOREGROUND_COLOR);
    }
    public void updateStatusBar() {
        healthField.setText("Health: " + GameManager.getPlayer().getHealth());
        goldField.setText("Gold: " + GameManager.getPlayer().getGold());
        panel.revalidate();
        panel.repaint();
    }
}
