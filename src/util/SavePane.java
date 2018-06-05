package util;

import entity.lifelessentity.item.Item;
import entity.livingentity.Monster;
import helper.Helper;
import main.GameManager;
import map.Map;
import map.level.Level;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class SavePane {

    private JPanel panel;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton dontSaveButton;
    private JLabel title;
    private JTextField nameField;

    private JFrame f;

    public SavePane() {
        setDefaults();

        f = new JFrame("Save Your Game?");
        f.setContentPane(panel);
        f.setVisible(true);
        f.setLocationRelativeTo(null);
        f.pack();
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        f.revalidate();
        f.repaint();

        f.setContentPane(panel);
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

        GameManager.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        nameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                try {
                    ObjectOutputStream output = new ObjectOutputStream(
                            new BufferedOutputStream(
                                    new FileOutputStream("/data/saves/" + nameField.getText())
                            )
                    );
                    output.writeObject(Monster.getMonsters());
                    output.writeObject(Item.items);
                    output.writeObject(Level.getLevel());

                    output.writeObject(GameManager.getPlayer());

                    output.writeObject(Map.getMap().getMessageBar());
                    output.writeObject(Map.getMap().getStatusBar());
                } catch (IOException i) {
                    i.printStackTrace();
                }
            }
        });
    }
}
