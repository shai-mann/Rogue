package entity.component;

import entity.lifeless.item.structure.Item;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private final List<Item> items = new ArrayList<>();
    private int gold = 0;

    public Inventory() {}

    public void addItems(Item...items) {
        this.items.addAll(List.of(items));
    }

    /**
     * Changes the amount of gold in this Inventory by the given delta. If the delta is negative,
     * returns the amount stolen from the Inventory (cannot be more than is in the inventory).
     * @param delta the amount to adjust the gold in this Inventory by.
     * @return 0 if gold was added, otherwise returns the amount stolen (as positive integer).
     */
    public int changeGold(int delta) {
        int currentGold = gold;

        gold += delta;
        gold = Math.max(gold, 0); // prevent gold from being negative

        if (delta > 0) return 0; // return 0 if delta was positive

        return Math.min(currentGold, -delta);
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
