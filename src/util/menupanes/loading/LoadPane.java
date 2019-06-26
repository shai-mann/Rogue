package util.menupanes.loading;

import helper.Helper;
import main.GameManager;
import util.gamepanes.saving.Save;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class LoadPane {

    private JPanel savedPanel;
    private JPanel panel;
    private JScrollPane scrollingPanel;
    private JPanel scrollablePanel;

    public LoadPane(JPanel oldPanel) {
        savedPanel = oldPanel;
        setDefaults();
        loadGames();

        GameManager.replaceContentPane(panel);
    }
    private void loadGames() {
        for (File f : Save.getSaves()) {
            scrollablePanel.add(new LoadGame(f.getName()).getPanel());
            scrollablePanel.revalidate();
        }
    }

    private void setDefaults() {
        Helper.setSize(panel, GameManager.getFrame().getSize());
        Helper.setSize(scrollingPanel, GameManager.getFrame().getSize());
        Helper.setSize(scrollablePanel, GameManager.getFrame().getSize());
        panel.setBackground(Helper.BACKGROUND_COLOR);
        scrollablePanel.setBackground(Helper.BACKGROUND_COLOR);
        scrollingPanel.setBackground(Helper.BACKGROUND_COLOR);
        scrollingPanel.getViewport().setBackground(Helper.BACKGROUND_COLOR);
        panel.setBorder(new TitledBorder(
                panel.getBorder(),
                "Load Game",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font(Helper.THEME_FONT, Font.PLAIN, 25),
                Helper.FOREGROUND_COLOR)
        );
        scrollingPanel.getVerticalScrollBar().setPreferredSize(new Dimension(0, Integer.MAX_VALUE));

        setMouseListener();
    }
    private void setMouseListener() {
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
    }

    private void createUIComponents() {
        scrollablePanel = new JPanel();
        scrollingPanel = new JScrollPane(scrollablePanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }
}
