package extra.inventory;

import entity.item.Item;
import helper.Helper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InventoryItem extends JComponent {
    private JPanel panel;
    private JButton useButton;
    private JButton throwButton;
    private JButton dropButton;
    private JLabel label;

    private static ArrayList<InventoryItem> inventoryItems = new ArrayList<>();

    public InventoryItem(int number, Item item) {
        setDefaults();
        inventoryItems.add(this);

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
    private void setDefaults() {
        panel.setBackground(Helper.BACKGROUND_COLOR);
        useButton.setBackground(Helper.BACKGROUND_COLOR);
        throwButton.setBackground(Helper.BACKGROUND_COLOR);
        dropButton.setBackground(Helper.BACKGROUND_COLOR);

        label.setFont(new Font(Helper.THEME_FONT, Font.PLAIN, 15));

        useButton.setVisible(false);
        throwButton.setVisible(false);
        dropButton.setVisible(false);
    }
}
