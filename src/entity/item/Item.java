package entity.item;

import entity.Entity;

import java.util.ArrayList;

public class Item extends Entity {

    public static ArrayList<Item> items = new ArrayList<Item>;

    public Item(String g, int x, int y) {
        super(g, x, y);
        items.add(this);
    }

}
