package extra.inventory;

import entity.item.Item;
import helper.Helper;
import main.GameManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class InventoryPane {
    private JPanel panel;
    private JScrollPane scrollablePane;
    private JPanel scrollingPanel;

    public InventoryPane() {
        setDefaults();
        addInventory();
        addMouseListener();

        GameManager.replaceContentPane(panel);
    }
    private void addInventory() {
        ArrayList<Item> playerInventory = GameManager.getPlayer().getInventory();
        for (int i = 0; i < playerInventory.size(); i++) {
            scrollingPanel.add(new InventoryItem(i + 1, playerInventory.get(i)).getPanel());
            panel.revalidate();
        }
    }
    private void addMouseListener() {
        scrollingPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    for (InventoryItem item : InventoryItem.getInventoryItems()) {
                        if (item.getPanel().getBounds().contains(e.getPoint())) {
                            item.setButtonsVisible(true);
                        } else {
                            item.setButtonsVisible(false);
                        }
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }
    private void setDefaults() {
        Helper.setSize(panel, GameManager.getFrame().getSize());
        panel.setBackground(Helper.BACKGROUND_COLOR);
        scrollingPanel.setBackground(Helper.BACKGROUND_COLOR);
        scrollingPanel.setLayout(new BoxLayout(scrollingPanel, BoxLayout.Y_AXIS));
        scrollablePane.getViewport().setBackground(Helper.BACKGROUND_COLOR);
        panel.setBorder(new TitledBorder(
                panel.getBorder(),
                "Inventory",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font(Helper.THEME_FONT, Font.PLAIN, 25),
                Helper.FOREGROUND_COLOR)
        );
    }
}
