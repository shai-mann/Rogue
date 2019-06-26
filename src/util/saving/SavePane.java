package util.saving;

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
        nameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: make this sad mess functional
                try {
                    File f = new File(new File(SavePane.class.getProtectionDomain().getCodeSource().getLocation().getPath())
                            + "/data/saves/" + nameField.getText());
                    f = new File(f.getPath().replace("%20", " "));
                    Files.createFile(f.toPath());
                    errorField.setVisible(false);
                    errorField.setText("");
                    ObjectOutputStream output = new ObjectOutputStream(
                            new BufferedOutputStream(new FileOutputStream(f.getPath()))
                    );
                    output.writeObject(Monster.getMonsters());
                    output.writeObject(Item.items);
                    output.writeObject(Level.getLevel().getHiddenTable());
                    output.writeObject(GameManager.getTable());

                    output.writeObject(GameManager.getPlayer());

                    output.writeObject(Map.getMap().getMessageBar().getMessages());
                } catch (IOException i) {
                    errorField.setText("An error occurred. That name may be taken");
                    nameField.setText("");
                    errorField.setVisible(true);
                    f.pack();
                    f.revalidate();
                    f.repaint();
                    i.printStackTrace();
                }
            }
        });
    }
}
