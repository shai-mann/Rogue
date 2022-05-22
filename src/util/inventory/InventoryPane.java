package util.inventory;

import entityimpl2.lifeless.item.combat.Arrow;
import entityimpl2.lifeless.item.structure.Item;
import main.GameManager;
import map.level.Level;
import util.Helper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class InventoryPane {
    private static JPanel savedPanel;
    private JPanel panel;
    private JScrollPane scrollablePane;
    private JPanel scrollingPanel;

    public InventoryPane(JPanel savePane) {
        savedPanel = savePane;
        addInventory();
        setDefaults();
        addMouseListener();

        GameManager.replaceContentPane(panel);
    }

    public InventoryPane(String message, MouseAdapter l, JPanel savePane) {
        savedPanel = savePane;
        addInventory();
        setDefaults();
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
        List<Item> playerInventory = Level.getLevel().getPlayer().getInventory();
        for (int i = 0; i < playerInventory.size(); i++) {
            InventoryItem iv = InventoryItem.checkDuplicity(playerInventory.get(i));
            if (iv != null) {
                if (iv.getItem() instanceof Arrow) {
                    iv.addArrowDuplicity(((Arrow) playerInventory.get(i)).amount());
                } else {
                    iv.addDuplicity();
                }
            } else {
                scrollingPanel.add(new InventoryItem(i + 1, playerInventory.get(i)).getPanel());
            }
            panel.validate();
        }
        panel.revalidate();
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
        scrollablePane.setViewportView(scrollingPanel);
        if (Level.getLevel().getPlayer().getInventory().size() > 0) {
            Helper.setSize(scrollingPanel, new Dimension(GameManager.getFrame().getWidth() - 5,
                    Level.getLevel().getPlayer().getInventory().size() * 76
            ));
        }

        scrollablePane.getVerticalScrollBar().setPreferredSize(new Dimension(10, Integer.MAX_VALUE));
        scrollablePane.getVerticalScrollBar().setUnitIncrement(16);

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
        Helper.setSize(panel, GameManager.getFrame().getSize());
        GameManager.getFrame().pack();
    }

    public static JPanel getSavedPane() {
        return savedPanel;
    }

    private void createUIComponents() {
        scrollablePane = new JScrollPane();
        scrollingPanel = new JPanel();
    }

}
