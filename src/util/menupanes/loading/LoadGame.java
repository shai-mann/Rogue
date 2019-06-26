package util.menupanes.loading;

import helper.Helper;
import main.GameManager;
import settings.Settings;
import util.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LoadGame {

    public static ArrayList<LoadGame> games = new ArrayList<>();

    private String name;
    private JPanel panel;
    private JButton loadButton;
    private JLabel nameLabel;
    private ImagePanel imagePanel;

    public LoadGame(String name) {
        this.name = fixName(name);
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

        nameLabel.setText("<html><u>"+ name + "</u></html>");
        nameLabel.setForeground(Color.RED);
        loadButton.setVisible(false);
        imagePanel.setBackground(Helper.BACKGROUND_COLOR);
        Helper.setSize(imagePanel, new Dimension(GameManager.getFrame().getWidth(), 100));
        Helper.setSize(panel, new Dimension(GameManager.getFrame().getWidth(), 100));
        panel.repaint();
    }
    private void setDefaultSettings(JComponent jc) {
        jc.setBackground(Helper.BACKGROUND_COLOR);
        jc.setForeground(Helper.FOREGROUND_COLOR);
        jc.setFont(new Font(Helper.THEME_FONT, Font.PLAIN,
                Settings.getTextSize() < 24 ? 24 : Settings.getTextSize()));
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
