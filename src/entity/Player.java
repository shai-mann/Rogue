package entity;

import map.level.Room;

import java.awt.*;
import entity.item.Armor;
import entity.item.Gold;
import entity.item.Item;
import entity.item.Ring;
import entity.monster.Monster;
import extra.GravePane;
import extra.inventory.InventoryPane;
import extra.MessageBar;
import helper.Helper;
import main.GameManager;
import map.Map;
import map.level.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class Player extends Entity implements KeyListener {

    private Map map;
    private Status status;
    private boolean showInventory = false;
    private Container savedContentPane;

    private Armor wornItem;
    private Item heldItem;
    private Ring leftRing;
    private Ring rightRing;

    private int gold = 0;
    private int experience = 0;

    private ArrayList<Item> inventory = new ArrayList<>();

    public Player(Room room) {
        super("@", 0, 0);
        setLocation(room);
        GameManager.getFrame().addKeyListener(this);
        status = new Status();
        status.setAc(8);
        map = Map.getMap();
    }

    // KEY LISTENER OVERRIDES

    public void keyPressed(KeyEvent e) {
        if (checkDeath()) {
            return;
        }
        boolean moved = false;
        if (!status.isParalyzed() && !status.isConfused()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                case KeyEvent.VK_UP:
                    moved = move(UP);
                    break;
                case KeyEvent.VK_S:
                case KeyEvent.VK_DOWN:
                    moved = move(DOWN);
                    break;
                case KeyEvent.VK_A:
                case KeyEvent.VK_LEFT:
                    moved = move(LEFT);
                    break;
                case KeyEvent.VK_D:
                case KeyEvent.VK_RIGHT:
                    moved = move(RIGHT);
                    break;
                default:
                    break;
            }
            if (e.getKeyCode() == KeyEvent.VK_I) {
                openOrCloseInventory();
            }
        }
        if (status.isConfused() || status.isDrunk()) {
            moved = moveRandom();
        }
        if (moved) {
            map.update();
        } else if (e.getKeyCode() != KeyEvent.VK_I){
            hitMonster();
        }
        status.update();
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    // KEY LISTENER HELPER METHODS

    private void openOrCloseInventory() {
        showInventory = !showInventory;
        if (showInventory) {
            savedContentPane = GameManager.getFrame().getContentPane();
            new InventoryPane();
        } else {
            GameManager.replaceContentPane((JPanel) savedContentPane);
        }
    }
    private void hitMonster() {
        for (int i = 0; i < Monster.getMonsters().size(); i++) {
            Monster monster = Monster.getMonsters().get(i);
            if (isNextTo(monster)) {
                double hitChance = (100 - ((10 - monster.getStatus().getAc()) * 3) + 30) / 100;
                if (Helper.random.nextDouble() <= hitChance) {
                    Status monsterStatus = monster.getStatus();
                    monsterStatus.setHealth(monsterStatus.getHealth() - getDamage());
                    map.update();
                    if (monster.health > 0) {
                        MessageBar.addMessage("You hit the " + monster.getName());
                    }
                    if (monster.getStatus().isSleeping()) {
                        monster.getStatus().setSleeping(0);
                    }
                }
            }
        }
    }
    private boolean moveRandom() {
        int[] directions = {UP,DOWN,RIGHT,LEFT};
        return move(directions[Helper.random.nextInt(directions.length)]);
    }

    // UPDATE METHODS

    public void update() {
        if (checkDeath()) {
            new GravePane();
        }
        checkToPickUpItem();
    }
    private void checkToPickUpItem() {
        if (overWrittenGraphic.equals("*")) {
            Gold foundGold = (Gold) Item.getItemAt(getXPos(), getYPos());

            overWrittenGraphic = foundGold.overWrittenGraphic;
            gold += foundGold.getAmount();

            MessageBar.addMessage("You picked up " + foundGold.getAmount() + " gold");
            Item.items.remove(foundGold);
        } else if (overWrittenGraphic.equals("]") || overWrittenGraphic.equals("&")) {
            Item foundItem = Item.getItemAt(getXPos(), getYPos());

            overWrittenGraphic = foundItem.overWrittenGraphic;

            inventory.add(foundItem);
            MessageBar.addMessage("You picked up " + foundItem.getName());
            Item.items.remove(foundItem);
        }
    }
    private boolean checkDeath() {
        return getHealth() <= 0;
    }

    // GETTERS

    public int getHealth() {
        return health;
    }
    public int getGold() {
        return gold;
    }
    public Status getStatus() {
        return status;
    }
    public ArrayList<Item> getInventory() {
        return inventory;
    }
    public int getExperienceDigitsNumber() {
        return String.valueOf(experience).length();
    }
    public int getExperience() {
        return experience;
    }
    public int getDamage() {
        // TODO: when swords are added, make it based on damage of heldItem
        return getStatus().isWeakened() ? 1 : 2;
    }

    // SETTERS

    public void addExperience(int experience) {
        this.experience += experience;
    }
    public void stealGold(int amount) {
        this.gold -= gold;
    }
    private void setLocation(Room room) {
        super.setLocation(room.getRandomPointInBounds());
    }
}
