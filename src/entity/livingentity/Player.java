package entity.livingentity;

import entity.Entity;
import entity.KeyBinds;
import entity.Status;
import entity.lifelessentity.Trap;
import entity.lifelessentity.item.*;
import entity.lifelessentity.item.combat.Armor;
import map.level.Door;
import map.level.Level;
import map.level.Room;

import java.awt.*;

import extra.GravePane;
import extra.inventory.InventoryPane;
import extra.MessageBar;
import helper.Helper;
import main.GameManager;
import map.Map;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

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
    private int stepsTakenSinceMeal = 0;
    private int hungerLevel = SATISFIED;

    private static final int SATISFIED = 0;
    private static final int HUNGRY = 1;
    private static final int STARVING = 2;
    private static final int WEAK = 3;

    private int maxHealth = Monster.DEFAULT_HEALTH;
    private int level = 1;
    private int[] levelingThresholds = {0, 10, 20, 40, 80, 160, 320, 640, 1280, 2560, 5120, 10240, 20480};

    private ArrayList<Item> inventory = new ArrayList<>();

    public Player(Room room) {
        super("@", 0, 0);
        setLocation(room);
        GameManager.getTable().setValueAt("", 0, 0);
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
        if (e.getKeyCode() == KeyBinds.INVENTORY) {
            toggleInventory();
        }
        if (!status.isParalyzed() && !status.isConfused() && !showInventory) {
            if(e.getKeyCode() == KeyBinds.MOVE_DOWN || e.getKeyCode() == KeyBinds.MOVE_DOWN_ALT) {
                moved = move(DOWN);
                if (!moved) {
                    hitMonster(DOWN);
                }
            } else if (e.getKeyCode() == KeyBinds.MOVE_UP || e.getKeyCode() == KeyBinds.MOVE_UP_ALT) {
                moved = move(UP);
                if (!moved) {
                    hitMonster(UP);
                }
            } else if (e.getKeyCode() == KeyBinds.MOVE_RIGHT || e.getKeyCode() == KeyBinds.MOVE_RIGHT_ALT) {
                moved = move(RIGHT);
                if (!moved) {
                    hitMonster(RIGHT);
                }
            } else if (e.getKeyCode() == KeyBinds.MOVE_LEFT || e.getKeyCode() == KeyBinds.MOVE_LEFT_ALT) {
                moved = move(LEFT);
                if (!moved) {
                    hitMonster(LEFT);
                }
            } else if (e.getKeyCode() == KeyBinds.USE_STAIRCASE) {
                if (overWrittenGraphic.equals("%")) {
                    changeLevel(Level.getLevel().getStaircase().getDirection());
                }
            } else if (e.getKeyCode() == KeyBinds.SEARCH) {
                moved = true;
                search();
            }
        }
        if (status.isConfused() || status.isDrunk()) {
            moved = moveRandom();
        }
        if (moved || getStatus().isParalyzed()) {
            map.update();
        }
        status.update();
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    // KEY LISTENER HELPER METHODS

    private void search() {
        for (Door door : Door.getDoors()) {
            if (door.isSecret() && isNextTo(door)) {
                if (Helper.random.nextInt(99) + 1 >= 75) {
                    door.reveal();
                }
            }
        }
        for (Trap trap : Trap.getTraps()) {
            if (trap.isHidden() && isNextTo(new Point(trap.getXPos(), trap.getYPos()))) {
                trap.reveal();
            }
        }
    }
    private boolean isNextTo(Point p) {
        return
                ((p.getX() + 1 == getXPos() || p.getX() - 1 == getXPos()) && p.getY() == getYPos()) ||
                        ((p.getY() + 1 == getYPos() || p.getY() - 1 == getYPos()) && p.getX() == getXPos());
    }
    public InventoryPane toggleInventory() {
        showInventory = !showInventory;
        if (showInventory) {
            savedContentPane = GameManager.getFrame().getContentPane();
            return new InventoryPane();
        } else {
            GameManager.replaceContentPane((JPanel) savedContentPane);
            return null;
        }
    }
    public InventoryPane toggleInventory(String message) {
        InventoryPane inv = toggleInventory();
        inv.setBorderTitle(message);
        return inv;
    }
    private void hitMonster(int direction) {
        for (int i = 0; i < Monster.getMonsters().size(); i++) {
            Monster monster = Monster.getMonsters().get(i);
            if (fakeMove(direction).getX() == monster.getXPos() && fakeMove(direction).getY() == monster.getYPos()) {
                double hitChance = (100 - ((10 - monster.getStatus().getAc()) * 3) + 30) / 100;
                if (Helper.random.nextDouble() <= hitChance) {
                    Status monsterStatus = monster.getStatus();
                    monsterStatus.setHealth(monsterStatus.getHealth() - getDamage());
                    map.update();
                    if (monster.health > 0) {
                        MessageBar.addMessage("You hit the " + monster.getName());
                    }
                    if (monster.getStatus().isSleeping()) {
                        monster.getStatus().setSleeping(false);
                        if (monster.getHiddenChar() != null) {
                            GameManager.getTable().setValueAt(monster.getHiddenChar(), monster.getYPos(), monster.getXPos());
                        }
                    }
                }
            }
        }
    }
    private boolean moveRandom() {
        int[] directions = {UP,DOWN,RIGHT,LEFT};
        return move(directions[Helper.random.nextInt(directions.length)]);
    }
    private void changeLevel(int direction) {
        Level.getLevel().newLevel(direction);
        setLocation(Level.getLevel().getStartingRoom());
    }

    // UPDATE METHODS

    public void update() {
        stepsTakenSinceMeal++;
        hungerLevel = updateHungerStatus();
        if (checkDeath()) {
            new GravePane();
        } else if (checkLevelUp()) {
            levelUp();
        }
        checkToPickUpItem();
    }
    private int updateHungerStatus() {
        if (stepsTakenSinceMeal > 100) {
            if (stepsTakenSinceMeal > 300) {
                if (stepsTakenSinceMeal > 550) {
                    if (stepsTakenSinceMeal > 600) {
                        health = 0;
                        return 4;
                    } else {
                        return WEAK;
                    }
                } else {
                    return STARVING;
                }
            } else {
                return HUNGRY;
            }
        } else {
            return SATISFIED;
        }
    }
    private void checkToPickUpItem() {
        if (overWrittenGraphic.equals("*")) {
            Gold foundGold = (Gold) Item.getItemAt(getXPos(), getYPos());
            if (foundGold == null) {
                return;
            }

            overWrittenGraphic = foundGold.overWrittenGraphic;
            gold += foundGold.getAmount();

            MessageBar.addMessage("You picked up " + foundGold.getAmount() + " gold");
            Item.items.remove(foundGold);
        } else if (overWrittenGraphic.equals("]") || overWrittenGraphic.equals("&") ||
                overWrittenGraphic.equals(":") || overWrittenGraphic.equals("?") ||
                overWrittenGraphic.equals("/") || overWrittenGraphic.equals("!")) {
            Item foundItem = Item.getItemAt(getXPos(), getYPos());
            if (foundItem == null) {
                return;
            }

            overWrittenGraphic = foundItem.overWrittenGraphic;

            inventory.add(foundItem);
            if (foundItem instanceof Food) {
                MessageBar.addMessage("You found some " + foundItem.getName().toLowerCase());
            } else if (foundItem instanceof Armor) {
                MessageBar.addMessage("You found " + foundItem.getName());
            } else {
                MessageBar.addMessage("You found a " + foundItem.getName());
            }
            Item.items.remove(foundItem);
        }
    }
    private boolean checkLevelUp() {
        return experience >= levelingThresholds[level];
    }
    private void levelUp() {
        level++;
        maxHealth *= 1.1;
        // increase maxDamage

        MessageBar.addMessage("Welcome to level " + level);
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
    private int getDamage() {
        // TODO: when swords are added, make it based on damage of heldItem
        return getStatus().isWeakened() ? 1 : 2;
    }
    public int getMaxHealth() {
        return maxHealth;
    }
    public int getLevelThreshold() {
        return levelingThresholds[level];
    }
    public String getHungerLevel() {
        switch (hungerLevel) {
            case 0:
                return "";
            case 1:
                return "hungry";
            case 2:
                return "starving";
            case 3:
                return "weak";
            default:
                return "";
        }
    }
    public Item getHeldItem() {
        return heldItem;
    }
    public Item getWornItem() {
        return wornItem;
    }

    // SETTERS

    public void addExperience(int experience) {
        this.experience += experience;
    }
    public void stealGold(int amount) {
        this.gold -= amount;
    }
    public void drainMaxHealth(int amount) {
        this.maxHealth -= amount;
        if (health < maxHealth) {
            health = maxHealth;
        }
    }
    public void setLocation(Room room) {
        super.setLocation(room.getRandomPointInBounds());
    }
    public void eat() {
        stepsTakenSinceMeal = 0;
        hungerLevel = updateHungerStatus();
    }
}
