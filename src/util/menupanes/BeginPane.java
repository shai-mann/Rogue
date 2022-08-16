package util.menupanes;

import main.GameManager;
import map.Game;
import util.Helper;
import util.ImagePanel;
import util.menupanes.loading.LoadPane;

import javax.swing.*;

public class BeginPane extends JComponent {

    private JPanel panel;
    private JButton newGameButton;
    private JButton loadGameButton;
    private JButton settingsButton;
    private JPanel imagePanel;

    public BeginPane() {
        setDefaults();

        GameManager.replaceContentPane(panel);
    }

    private void setDefaults() {
        imagePanel.setBackground(Helper.BACKGROUND_COLOR);
        Helper.setSize(panel, Helper.getScreenSize());

        newGameButton.setBackground(Helper.BACKGROUND_COLOR);
        newGameButton.setForeground(Helper.FOREGROUND_COLOR);

        loadGameButton.setBackground(Helper.BACKGROUND_COLOR);
        loadGameButton.setForeground(Helper.FOREGROUND_COLOR);

        settingsButton.setBackground(Helper.BACKGROUND_COLOR);
        settingsButton.setForeground(Helper.FOREGROUND_COLOR);

        addActionListeners();
    }

    private void addActionListeners() {
        newGameButton.addActionListener(e -> {
            new Game();
            GameManager.getFrame().requestFocus();
        });
        loadGameButton.addActionListener(e -> new LoadPane(panel));
        settingsButton.addActionListener(e -> new SettingsPane(panel));
    }

    private void createUIComponents() {
        imagePanel = new ImagePanel("data/images/Rogue_Background.PNG", GameManager.getFrame());
    }

}
