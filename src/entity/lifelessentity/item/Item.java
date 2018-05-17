package entity.lifelessentity.item;

import com.sun.istack.internal.Nullable;
import entity.Entity;
import entity.lifelessentity.item.combat.Armor;
import entity.lifelessentity.item.combat.Arrow;
import entity.lifelessentity.item.combat.Weapon;
import util.MessageBar;
import helper.Helper;
import map.level.Room;

import java.awt.*;
import java.util.ArrayList;

public class Item extends Entity {

    public static ArrayList<Item> items = new ArrayList<>();
    public String name;
    public String hiddenName;
    public enum itemTypes { ARMOR, GOLD, FOOD, RING, POTION, WAND, SCROLL, WEAPON, ARROW }

    public Item(String g, int x, int y) {
        super(g, x, y);
        items.add(this);
    }

    public void use() {

    }
    public void identify() {
        name = hiddenName;
        MessageBar.addMessage("You identified a " + name);
    }

    // GETTER METHODS

    private static ArrayList<Item> getAllItemsOfType(itemTypes type) {
        ArrayList<Item> itemsList = (ArrayList<Item>) items.clone();
        switch (type) {
            case ARMOR:
                for (int i = 0; i < itemsList.size();) {
                    Item item = itemsList.get(i);
                    if(!(item instanceof Armor)) {
                        itemsList.remove(item);
                    } else {
                        i++;
                    }
                }
                break;
            case GOLD:
                for (int i = 0; i < itemsList.size();) {
                    Item item = itemsList.get(i);
                    if(!(item instanceof entity.lifelessentity.item.Gold)) {
                        itemsList.remove(item);
                    } else {
                        i++;
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
                    } else {
                        i++;
                    }
                }
                break;
            case WAND:
                for (int i = 0; i < itemsList.size();) {
                    Item item = itemsList.get(i);
                    if(!(item instanceof Wand)) {
                        itemsList.remove(item);
                    } else {
                        i++;
                    }
                }
                break;
        }
        return itemsList;
    }
    public static ArrayList<Item> getAllItemsOfTypes(ArrayList<itemTypes> types, @Nullable ArrayList<Item> items) {
        ArrayList<Item> itemsList;
        if (items == null) {
            itemsList = new ArrayList<>();
        } else {
            itemsList = items;
        }
        for (itemTypes type : types) {
            itemsList.addAll(getAllItemsOfType(type));
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
    public String getHiddenName() {
        return hiddenName;
    }

    // STATIC METHODS

    public static void randomizeHiddenNames() {
        Ring.randomizeObfuscatedNames();
        Wand.randomizeObfuscatedNames();
        Potion.randomizeObfuscatedNames();
    }

    // ITEM SPAWNING METHODS

    public static void spawnItems() {
        // TODO: Update to use Poisson's thingy
        for (Room room : Room.rooms) {
            if (Helper.random.nextInt(99) + 1 >= 45) {
                spawnItem(room, getRandomItemType());
            }
        }
    }
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
                new Food(location.x, location.y);
                break;
            case RING:
                new Ring(location.x, location.y);
                break;
            case WAND:
                new Wand(location.x, location.y);
                break;
            case POTION:
                new Potion(location.x, location.y);
                break;
            case SCROLL:
                new Scroll(location.x, location.y);
                break;
            case ARROW:
                new Arrow(location.x, location.y);
                break;
            case WEAPON:
                new Weapon(null, location.x, location.y);
                break;
        }
    }
    private static void spawnItemCopy(Item item) {
        if (item instanceof Armor) {
            new Armor((Armor) item);
        } else if (item instanceof Ring) {
            new Ring((Ring) item);
        } else if (item instanceof Food) {
            new Food((Food) item);
        } else if (item instanceof Potion) {
            new Potion((Potion) item);
        } else if (item instanceof Scroll) {
            new Scroll((Scroll) item);
        } else if (item instanceof Wand) {
            new Wand((Wand) item);
        }
    }
    public static void spawnItem(int x, int y, @Nullable itemTypes type, @Nullable Item item) {
        if (item != null) {
            spawnItemCopy(item);
        } else {
            if (type == null) {
                spawnItem(new Point(x, y), getRandomItemType());
            } else {
                spawnItem(new Point(x, y), type);
            }
        }
    }
    private static void spawnItem(Point p, itemTypes type) {
        switch (type) {
            case WAND:
                new Wand(p.x, p.y);
                break;
            case RING:
                new Ring(p.x, p.y);
                break;
            case GOLD:
                new Gold(p.x, p.y);
                break;
            case ARMOR:
                new Armor(p.x, p.y);
                break;
            case FOOD:
                new Food(p.x, p.y);
                break;
            case POTION:
                new Potion(p.x, p.y);
                break;
            case SCROLL:
                new Scroll(p.x, p.y);
                break;
        }
    }
    private static itemTypes getRandomItemType() {
        // TODO: make this weighted per item
        return itemTypes.values()[Helper.random.nextInt(itemTypes.values().length)];
    }
}
