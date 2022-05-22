package util.menupanes.loading;

import entityimpl2.lifeless.Staircase;
import main.GameManager;
import map.Map;
import map.level.Level;
import map.level.Room;
import map.level.table.CustomRoomTable;
import settings.Settings;
import util.Helper;
import util.ImagePanel;
import util.animation.Animation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class LoadGame {

    public static ArrayList<LoadGame> games = new ArrayList<>();

    private String name;
    private JPanel panel;
    private JButton loadButton;
    private JLabel nameLabel;
    private ImagePanel imagePanel;

    private String filePath;

    public LoadGame(String name, String filePath) {
        this.name = fixName(name);
        this.filePath = filePath;
        setDefaults();
        games.add(this);
    }

    private String fixName(String name) {
        name = name.replace("_", " ");
        String[] words = name.split(" ");
        name = "";
        for (String s : words) {
            s = s.substring(0, 1).toUpperCase().concat(s.substring(1, s.length()));
            name = name.concat(s) + " ";
        }
        return name.substring(0, name.length() - 1);
    }

    private void setDefaults() {
        setDefaultSettings(panel);
        setDefaultSettings(nameLabel);
        setDefaultSettings(loadButton);

        nameLabel.setText("<html><u>" + name + "</u></html>");
        nameLabel.setForeground(Color.RED);
        loadButton.setVisible(false);
        imagePanel.setBackground(Helper.BACKGROUND_COLOR);
        Helper.setSize(imagePanel, new Dimension(GameManager.getFrame().getWidth(), 80));
        Helper.setSize(panel, new Dimension(GameManager.getFrame().getWidth(), 80));

        addActionListener();
    }

    private void setDefaultSettings(JComponent jc) {
        jc.setBackground(Helper.BACKGROUND_COLOR);
        jc.setForeground(Helper.FOREGROUND_COLOR);
        jc.setFont(new Font(Helper.THEME_FONT, Font.PLAIN,
                Settings.getTextSize() < 24 ? 24 : Settings.getTextSize()));
    }

    private void addActionListener() {
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loadMonsters();
                    loadItems();
                    loadPlayer();
                    loadRooms();
                    loadLevel();
                    loadMap(); // must come after level
                } catch (IOException | ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
                Level.getLevel().getPlayer().apply((JPanel) GameManager.getFrame().getContentPane());
                GameManager.getFrame().pack();
                GameManager.getFrame().repaint();
                GameManager.getFrame().requestFocus();
            }
        });
    }

    // LOADING HELPERS

    private void loadMap() throws IOException, ClassNotFoundException {
        ArrayList<Animation> animations = (ArrayList<Animation>)
                new ObjectInputStream(new FileInputStream(
                        filePath + "/animations")).readObject();
        String[] messages;
        try {
            messages = (String[]) new ObjectInputStream(new FileInputStream(
                    filePath + "/messages"
            )).readObject();
        } catch (ClassCastException e) {
            messages = new String[]{(String) new ObjectInputStream(new FileInputStream(
                    filePath + "/messages"
            )).readObject()};
        }
        new Map(animations, messages);
    }

    private void loadLevel() throws IOException, ClassNotFoundException {
        CustomRoomTable hiddenTable = (CustomRoomTable) new ObjectInputStream(new FileInputStream(
                filePath + "/hidden_table")).readObject();
        CustomRoomTable shownTable = (CustomRoomTable) new ObjectInputStream(new FileInputStream(
                filePath + "/shown_table")).readObject();
        ObjectInputStream os = new ObjectInputStream(new FileInputStream(
                filePath + "/level_data"));
        Staircase staircase = (Staircase) os.readObject();
        int direction = (int) os.readObject();
        int levelNumber = (int) os.readObject();
        Room startingRoom = (Room) os.readObject();

//        new Level(
//                hiddenTable, shownTable, staircase, direction, levelNumber, startingRoom
//        );
    }

    private void loadMonsters() throws IOException, ClassNotFoundException {
//        Monster.setMonsters((ArrayList<Monster>) new ObjectInputStream(
//                new FileInputStream(filePath + "/monsters")
//        ).readObject());
    }

    private void loadItems() throws IOException, ClassNotFoundException {
//        Item.items = (ArrayList<Item>) new ObjectInputStream(
//                new FileInputStream(filePath + "/items")
//        ).readObject();
    }

    private void loadPlayer() throws IOException, ClassNotFoundException {
//        GameManager.setPlayer((Player) new ObjectInputStream(
//                new FileInputStream(filePath + "/player")
//        ).readObject());
    }

    private void loadRooms() throws IOException, ClassNotFoundException {
        Room.rooms = (ArrayList<Room>) new ObjectInputStream(new FileInputStream(filePath + "/rooms")).readObject();
    }

    private void createUIComponents() {
        panel = new JPanel();
        imagePanel = new ImagePanel("data/images/Rogue_Background.PNG", panel);
    }

    public void setButtonVisibility(boolean visible) {
        loadButton.setVisible(visible);
    }

    public JPanel getPanel() {
        return panel;
    }

}
