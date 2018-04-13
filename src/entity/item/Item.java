package entity.item;

import entity.Entity;

import java.awt.*;
import java.util.ArrayList;

public class Item extends Entity {

    public static ArrayList<Item> items = new ArrayList<>();
    public String name;

    public Item(String g, int x, int y) {
        super(g, x, y);
        items.add(this);
    }
    public static Item getItemAt(Point p) {
        for (Item item : items) {
            if (item.getXPos() == p.getX() && item.getYPos() == p.getY()) {
                return item;
            }
        }
        return null;
    }
    public static Item getItemAt(int x, int y) {
        for (Item item : items) {
            if (item.getXPos() == x && item.getYPos() == y) {
                return item;
            }
        }
        return null;
    }
    public String getName() {
        return name;
    }
    public static void randomizeHiddenNames() {
        Ring.randomizeObfuscatedNames();
    }
}
