package extra;

import entity.livingentity.Monster;
import helper.Helper;
import main.GameManager;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JComponent {
    private JLabel healthField;
    private JPanel panel;
    private JLabel goldField;
    private JLabel experienceLabel;

    // This class is what takes the player's stats and displays them at the bottom of the screen
    public StatusBar() {

        setDefaults();
    }
    public void setDefaults() {
        panel.setForeground(Helper.BACKGROUND_COLOR);
        panel.setBackground(Helper.BACKGROUND_COLOR);
        panel.setPreferredSize(new Dimension(GameManager.getFrame().getWidth(), (int) (GameManager.getFrame().getHeight() * 0.05)));
        panel.setMaximumSize(new Dimension(GameManager.getFrame().getWidth(), (int) (GameManager.getFrame().getHeight() * 0.05)));
        panel.setMinimumSize(new Dimension(GameManager.getFrame().getWidth(), (int) (GameManager.getFrame().getHeight() * 0.05)));

        healthField.setText("Health: " + Monster.DEFAULT_HEALTH);
        healthField.setForeground(Helper.FOREGROUND_COLOR);
        goldField.setText("Gold: 0");
        goldField.setForeground(Helper.FOREGROUND_COLOR);
        experienceLabel.setText("Experience: 0");
        experienceLabel.setForeground(Helper.FOREGROUND_COLOR);
    }
    public void updateStatusBar() {
        healthField.setText("Health: " + GameManager.getPlayer().getHealth());
        goldField.setText("Gold: " + GameManager.getPlayer().getGold());
        experienceLabel.setText("Experience: " + GameManager.getPlayer().getExperience());
        panel.revalidate();
        panel.repaint();
    }
}
