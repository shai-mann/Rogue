package util;

import helper.Helper;
import main.GameManager;
import map.Map;
import map.level.Level;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Map();
                GameManager.createPlayer();
                Level.getLevel().finalSetup();
                GameManager.getFrame().requestFocus();
            }
        });
        loadGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: add code to load games
            }
        });
        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SettingsPane(panel);
            }
        });
    }

    private void createUIComponents() {
        imagePanel = new ImagePanel();
    }
}
