package util.gamepanes.saving;

import entity.lifelessentity.item.Item;
import entity.livingentity.Monster;
import helper.Helper;
import main.GameManager;
import map.Map;
import map.level.Level;
import map.level.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;

public class SavePane {

    private JPanel panel;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton dontSaveButton;
    private JLabel title;
    private JTextField nameField;
    private JLabel errorField;

    private JFrame f;

    public SavePane() {
        f = new JFrame("Save Your Game?");
        f.setContentPane(panel);

        setDefaults();

        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        f.validate();
        f.repaint();
    }
    private void setDefaults() {
        panel.setBackground(Helper.BACKGROUND_COLOR);
        panel.setForeground(Helper.FOREGROUND_COLOR);

        title.setFont(new Font(Helper.THEME_FONT, Font.PLAIN, Font.BOLD));
        title.setForeground(Helper.FOREGROUND_COLOR);
        title.setBackground(Helper.BACKGROUND_COLOR);

        saveButton.setForeground(Helper.FOREGROUND_COLOR);
        saveButton.setBackground(Helper.BACKGROUND_COLOR);

        cancelButton.setForeground(Helper.FOREGROUND_COLOR);
        cancelButton.setBackground(Helper.BACKGROUND_COLOR);

        dontSaveButton.setForeground(Helper.FOREGROUND_COLOR);
        dontSaveButton.setBackground(Helper.BACKGROUND_COLOR);

        nameField.setText("Enter a name for your save:");
        nameField.setVisible(false);

        addListeners();
    }
    private void addListeners() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nameField.setVisible(true);
                panel.revalidate();
                panel.repaint();
            }
        });
        dontSaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameManager.getFrame().dispose();
                System.exit(0);
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                GameManager.getFrame().requestFocus();
            }
        });
        nameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (nameField.getText().equals("Enter a name for your save:")) {
                    nameField.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (nameField.getText().equals("")) {
                    nameField.setText("Enter a name for your save:");
                }
            }
        });
        nameField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }
            @Override
            public void keyPressed(KeyEvent e) {

            }
            @Override
            public void keyReleased(KeyEvent e) {
                boolean duplicate = false;
                if (Save.getSaves() != null) {
                    for (File f : Save.getSaves()) {
                        if (f.getName().equals(nameField.getText())) {
                            duplicate = true;
                        }
                    }
                }
                if (duplicate) {
                    errorField.setText("You are going to overwrite an existing save.");
                    errorField.setVisible(true);
                } else {
                    errorField.setVisible(false);
                }
                panel.repaint();
            }
        });
        nameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                * This function saves:
                *   Monsters
                *   Player
                *   Items
                *   Hidden and revealed tables
                *   Messages
                *   Staircase
                *   Level data
                *   Animations
                 */
                try {
                    File f = new File(Save.getCorrectPath() + nameField.getText());
                    f = new File(f.getPath().replace("%20", " "));
                    if (!f.exists()) {
                        Files.createDirectory(f.toPath());
                    }
                    writeSave(nameField.getText());

                } catch (IOException i) {
                    i.printStackTrace();
                }
            }
        });
    }

    private void writeSave(String dirPath) {
        new Save(dirPath + "/monsters", Monster.getMonsters());
        new Save(dirPath + "/items", Item.items);
        new Save(dirPath + "/hidden_table", Level.getLevel().getHiddenTable());
        new Save(dirPath + "/shown_table", Level.getLevel().getShownTable());
        new Save(dirPath + "/player", GameManager.getPlayer());
        new Save(dirPath + "/messages", (Object[]) Map.getMap().getMessageBar().getMessages());
        new Save(dirPath + "/animations", Map.getMap().getAnimationManager().getAnimations());
        new Save(dirPath + "/rooms", Room.rooms);
        // all of the extra bits and pieces of the level
        // consider adding hidden and shown tables to this list
        new Save(dirPath + "/level_data",
                Level.getLevel().getStaircase(),
                Level.getLevel().getDirection(),
                Level.getLevel().getLevelNumber(),
                Level.getLevel().getStartingRoom(),
                Level.getLevel().getShownPoints(),
                Level.getLevel().getBlindnessPoints()
        );

        Map.getMap().setSaved(true);
        f.dispose();
    }
}
