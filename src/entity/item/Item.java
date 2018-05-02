package entity.item;

import com.sun.istack.internal.Nullable;
import entity.Entity;
import map.level.Level;
import map.level.Room;

import java.awt.*;
import java.util.ArrayList;

public class Item extends Entity {

    public static ArrayList<Item> items = new ArrayList<>();
    public String name;
    public enum itemTypes { ARMOR, GOLD, FOOD, RING, STAFF, WAND }

    public Item(String g, int x, int y) {
        super(g, x, y);
        items.add(this);
    }

    // GETTER METHODS

    public static Item getClosestItem(Entity entity, @Nullable ArrayList<Item> i) {
        Item closestItem = null;
        if (i != null) {
            for (Item item : i) {
                if (closestItem == null || item.getDistanceTo(entity) < closestItem.getDistanceTo(entity)) {
                    closestItem = item;
                }
            }
        } else {
            for (Item item : items) {
                if (closestItem == null || item.getDistanceTo(entity) < closestItem.getDistanceTo(entity)) {
                    closestItem = item;
                }
            }
        }
        return closestItem;
    }
    public static ArrayList<Item> getAllItemsOfType(itemTypes type) {
        ArrayList<Item> itemsList = (ArrayList<Item>) items.clone();
        switch (type) {
            case ARMOR:
                for (int i = 0; i < itemsList.size();) {
                    Item item = itemsList.get(i);
                    if(!(item instanceof Armor)) {
                        itemsList.remove(item);
                    }
                }
                break;
            case GOLD:
                for (int i = 0; i < itemsList.size();) {
                    Item item = itemsList.get(i);
                    if(!(item instanceof Gold)) {
                        itemsList.remove(item);
                    }
                }
                break;
            case FOOD:
                break;
            case RING:
                for (int i = 0; i < itemsList.size();) {
                    Item item = itemsList.get(i);
                    if(!(item instanceof Ring)) {
                        itemsList.remove(item);
                    }
                }
                break;
            case STAFF:
                break;
            case WAND:
                for (int i = 0; i < itemsList.size();) {
                    Item item = itemsList.get(i);
                    if(!(item instanceof Wand)) {
                        itemsList.remove(item);
                    }
                }
                break;
        }
        return itemsList;
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

    // STATIC METHODS

    private static void spawnItem(Room room, itemTypes itemType) {
        Point location = room.getRandomPointInBounds();

        switch (itemType) {
            case ARMOR:
                new Armor(location.x, location.y);
                break;
            case GOLD:
                new Gold(location.x, location.y);
                break;
            case FOOD:
                // new Food
                break;
            case RING:
                new Ring(location.x, location.y);
                break;
            case STAFF:
                // new Staff
                break;
            case WAND:
                // new Wand
                break;
        }
    }
    public static void spawnItems() {
        // TODO: make spawning code for items in rooms
        spawnItem(Level.getLevel().getStartingRoom(), itemTypes.GOLD);
        spawnItem(Level.getLevel().getStartingRoom(), itemTypes.ARMOR);
        spawnItem(Level.getLevel().getStartingRoom(), itemTypes.RING);
    }
    public static void randomizeHiddenNames() {
        Ring.randomizeObfuscatedNames();
    }
}
