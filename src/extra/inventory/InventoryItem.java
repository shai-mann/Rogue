package extra.inventory;

import entity.item.Item;
import helper.Helper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class InventoryItem extends JComponent {
    private JPanel panel;
    private JButton useButton;
    private JButton throwButton;
    private JButton dropButton;
    private JLabel label;

    private Item item;

    private static ArrayList<InventoryItem> inventoryItems = new ArrayList<>();

    public InventoryItem(int number, Item item) {
        setDefaults();
        inventoryItems.add(this);
        this.item = item;

        label.setText(number + ") " + item.getName());
    }
    public JPanel getPanel() {
        return panel;
    }
    public void setButtonsVisible(boolean visible) {
        useButton.setVisible(visible);
        throwButton.setVisible(visible);
        dropButton.setVisible(visible);
    }
    public static ArrayList<InventoryItem> getInventoryItems() {
        return inventoryItems;
    }
    public Item getItem() {
        return item;
    }
    private void setDefaults() {
        panel.setBorder(new TitledBorder(""));

        panel.setBackground(Helper.BACKGROUND_COLOR);
        useButton.setBackground(Helper.BACKGROUND_COLOR);
        throwButton.setBackground(Helper.BACKGROUND_COLOR);
        dropButton.setBackground(Helper.BACKGROUND_COLOR);

        useButton.setForeground(Helper.FOREGROUND_COLOR);
        throwButton.setForeground(Helper.FOREGROUND_COLOR);
        dropButton.setForeground(Helper.FOREGROUND_COLOR);

        label.setFont(new Font(Helper.THEME_FONT, Font.BOLD, 20));
        label.setForeground(Color.WHITE);

        useButton.setVisible(false);
        throwButton.setVisible(false);
        dropButton.setVisible(false);
    }
}
