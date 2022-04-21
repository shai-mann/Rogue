package entity.component;

import entity.lifelessentity.item.Gold;
import entity.lifelessentity.item.Item;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private final List<Item> items = new ArrayList<>();
    private int gold = 0;

    public Inventory() {}

    public void addItems(Item...items) {
        this.items.addAll(List.of(items));
    }

    public void addGold(int gold) {
        this.gold += gold;
    }
    public void addGold(Gold g) {
        addGold(g.getAmount());
    }
    public void setGold(int amount) {
        this.gold = amount;
    }

    public boolean hasItems() {
        return !items.isEmpty();
    }

    public List<Item> getItems() {
        return items;
    }
    public int getGold() {
        return gold;
    }

}
