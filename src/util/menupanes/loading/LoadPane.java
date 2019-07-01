package util.menupanes.loading;

import helper.Helper;
import main.GameManager;
import settings.Settings;
import util.gamepanes.saving.Save;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class LoadPane {

    private JPanel savedPanel;
    private JPanel panel;
    private JScrollPane scrollingPanel;
    private JPanel scrollablePanel;
    private JButton cancelButton;

    public LoadPane(JPanel oldPanel) {
        savedPanel = oldPanel;
        loadGames();
        setDefaults();

        GameManager.replaceContentPane(panel);
    }
    private void loadGames() {
        for (File f : Save.getSaves()) {
            scrollablePanel.add(new LoadGame(f.getName(), f.getPath()).getPanel());
            scrollingPanel.validate();
        }
        scrollablePanel.add(cancelButton);
        scrollablePanel.revalidate();
    }

    private void setDefaults() {
        Helper.setSize(panel, GameManager.getFrame().getSize());
        scrollingPanel.setViewportView(scrollablePanel);
        if (LoadGame.games.size() > 0) {
            Helper.setSize(scrollablePanel, new Dimension(GameManager.getFrame().getWidth() - 5,
                    LoadGame.games.size() * 90 + 50
            ));
        }

        scrollingPanel.getVerticalScrollBar().setUnitIncrement(16);
        scrollingPanel.getVerticalScrollBar().setPreferredSize(new Dimension(0, Integer.MAX_VALUE));

        panel.setBackground(Helper.BACKGROUND_COLOR);
        scrollablePanel.setBackground(Helper.BACKGROUND_COLOR);
        scrollingPanel.setBackground(Helper.BACKGROUND_COLOR);
        scrollingPanel.getViewport().setBackground(Helper.BACKGROUND_COLOR);
        cancelButton.setBackground(Helper.BACKGROUND_COLOR);
        cancelButton.setForeground(Helper.FOREGROUND_COLOR);

        Helper.setSize(cancelButton, new Dimension(250, 50));
        cancelButton.setFont(new Font(Helper.THEME_FONT, Font.PLAIN,
                Settings.getTextSize() < 24 ? 24 : Settings.getTextSize()));

        panel.setBorder(new TitledBorder(
                panel.getBorder(),
                "Load Game",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font(Helper.THEME_FONT, Font.PLAIN, 25),
                Helper.FOREGROUND_COLOR)
        );

        addListeners();
    }
    private void addListeners() {
        scrollablePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    for (LoadGame g : LoadGame.games) {
                        if (g.getPanel().getBounds().contains(e.getPoint())) {
                            g.setButtonVisibility(true);
                        } else {
                            g.setButtonVisibility(false);
                        }
                    }
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoadGame.games.clear();
                GameManager.replaceContentPane(savedPanel);
            }
        });
    }

    private void createUIComponents() {
        scrollablePanel = new JPanel();
        scrollingPanel = new JScrollPane();
        cancelButton = new JButton("Cancel");
    }
}
