package util.inventory;

import entity.lifelessentity.item.*;
import entity.lifelessentity.item.combat.Armor;
import entity.lifelessentity.item.combat.Arrow;
import entity.lifelessentity.item.combat.Weapon;
import map.Map;
import settings.Settings;
import util.gamepanes.MessageBar;
import util.Helper;
import main.GameManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class InventoryItem extends JComponent {
    private JPanel panel;
    private JButton useButton;
    private JButton throwButton;
    private JButton dropButton;
    private JLabel label;

    private Item item;
    private int duplicity = 1;
    private String defaultText = "";

    private static ArrayList<InventoryItem> inventoryItems = new ArrayList<>();

    public InventoryItem(int number, Item item) {
        setDefaults();
        inventoryItems.add(this);
        this.item = item;
        setNames();
        duplicity = 1;

        if (item instanceof Wand) {
            label.setText(number + ") " + item.getName() + " Uses: " + ((Wand) item).getUses());
        } else {
            label.setText(number + ") " + item.getName());
        }
        defaultText = label.getText();
    }
    public JPanel getPanel() {
        return panel;
    }
    public void setButtonsVisible(boolean visible) {
        useButton.setVisible(visible);
        dropButton.setVisible(visible);
        if (item instanceof Weapon && ((Weapon) item).isThrowable()) {
            throwButton.setVisible(visible);
        }
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

        if (Settings.getTextSize() == Settings.TEXT_SIZE) {
            label.setFont(new Font(Helper.THEME_FONT, Font.BOLD, 20));
        } else {
            label.setFont(new Font(Helper.THEME_FONT, Font.BOLD, Settings.getTextSize()));
        }
        label.setForeground(Color.WHITE);

        Helper.setSize(panel, new Dimension(GameManager.getFrame().getWidth(), 75));

        useButton.setVisible(false);
        throwButton.setVisible(false);
        dropButton.setVisible(false);

        addActionListeners();
    }
    private void addActionListeners() {
        useButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameManager.getPlayer().toggleInventory();
                Map.getMap().update();
                GameManager.getPlayer().getStatus().update();
                item.use();
                GameManager.getFrame().requestFocus();
            }
        });
        dropButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!(item instanceof Armor && ((Armor) item).isBeingWorn()) &&
                        !(item.equals(GameManager.getPlayer().getHeldItem()))) {
                    GameManager.getPlayer().getInventory().remove(item);
                    if (item instanceof Armor) {
                        new Armor((Armor) item);
                    } else if (item instanceof Ring) {
                        new Ring((Ring) item);
                    } else if (item instanceof Scroll) {
                        new Scroll((Scroll) item);
                    } else if (item instanceof Potion) {
                        new Potion((Potion) item);
                    } else if (item instanceof Food) {
                        new Food((Food) item);
                    } else if (item instanceof Wand) {
                        new Wand((Wand) item);
                    } else if (item instanceof Arrow) {
                        new Arrow((Arrow) item);
                    } else if (item instanceof Weapon) {
                        new Weapon((Weapon) item);
                    }
                    GameManager.getPlayer().setOverwrittenGraphic(item.graphic);
                    GameManager.getFrame().requestFocus();
                    MessageBar.addMessage("You drop the " + getName());
                } else {
                    MessageBar.addMessage("You can't drop things you are wearing or holding.");
                }
                GameManager.getPlayer().toggleInventory();
            }
        });
    }
    private void setNames() {
        if (item instanceof Armor || item instanceof Ring) {
            if (item instanceof Armor && ((Armor) item).isBeingWorn()) {
                useButton.setText("Remove");
            } else {
                useButton.setText("Wear");
            }
            if (item instanceof Ring && GameManager.getPlayer().getRings().contains(item)) {
                useButton.setText("Remove");
            }
        } else if (item instanceof Potion) {
            useButton.setText("Drink");
        } else if (item instanceof Food) {
            useButton.setText("Eat");
        } else if (item instanceof Scroll) {
            useButton.setText("Read");
        } else if (item instanceof Weapon) {
            if (!((Weapon) item).isHeld()) {
                useButton.setText("Hold");
            } else {
                useButton.setText("Put away");
            }
        }
    }
    public void addDuplicity() {
        duplicity++;
        label.setText(defaultText + " (x" + duplicity + ")");
    }
    public void addArrowDuplicity(int arrows) {
        duplicity = ((Arrow) getItem()).getAmount() + arrows;
        label.setText(defaultText.substring(0, 2) + " " + duplicity + " arrows");
    }

    public static InventoryItem checkDuplicity(Item i) {
        for (InventoryItem it : inventoryItems) {
            if (it.getItem() != i && it.duplicity == 1 &&
                    it.getItem().getName().equals(i.getName())) {
                return it;
            } else if (it.getItem() instanceof Arrow && i instanceof Arrow) {
                return it;
            }
        }
        return null;
    }
}
