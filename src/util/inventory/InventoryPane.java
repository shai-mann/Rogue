package util.inventory;

import entity.lifelessentity.item.Item;
import helper.Helper;
import main.GameManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class InventoryPane {
    private static JPanel savedPanel;
    private JPanel panel;
    private JScrollPane scrollablePane;
    private JPanel scrollingPanel;

    public InventoryPane(JPanel savePane) {
        savedPanel = savePane;
        setDefaults();
        addInventory();
        addMouseListener();

        GameManager.replaceContentPane(panel);
    }
    public InventoryPane(String message, MouseAdapter l, JPanel savePane) {
        setDefaults();
        addInventory();
        setBorderTitle(message);
        scrollingPanel.addMouseListener(l);

        GameManager.replaceContentPane(panel);
    }
    public void setBorderTitle(String borderTitle) {
        panel.setBorder(new TitledBorder(borderTitle));
        panel.revalidate();
        panel.repaint();
    }
    private void addInventory() {
        ArrayList<Item> playerInventory = GameManager.getPlayer().getInventory();
        for (int i = 0; i < playerInventory.size(); i++) {
            scrollingPanel.add(new InventoryItem(i + 1, playerInventory.get(i)).getPanel());
            panel.revalidate();
        }
    }
    private void addMouseListener() {
        scrollingPanel.addMouseListener(new MouseAdapter() {
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

    private void createUIComponents() {
        scrollablePane = new JScrollPane(scrollingPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    public static JPanel getSavedPane() {
        return savedPanel;
    }
}
