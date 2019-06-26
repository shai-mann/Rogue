package entity.livingentity;

import entity.Effect;
import entity.Entity;
import entity.Status;
import entity.lifelessentity.Trap;
import entity.lifelessentity.item.*;
import entity.lifelessentity.item.combat.Armor;
import entity.lifelessentity.item.combat.Arrow;
import entity.lifelessentity.item.combat.Weapon;
import settings.Settings;
import util.inventory.InventoryItem;
import map.level.Door;
import map.level.Level;
import map.level.Room;

import java.awt.*;

import util.GravePane;
import util.inventory.InventoryPane;
import util.MessageBar;
import helper.Helper;
import main.GameManager;
import map.Map;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private int regenStepsCounter = 0;

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
        status = new Status(this);
        getStatus().setAc(9);
        map = Map.getMap();
    }

    // KEY LISTENER OVERRIDES

    public void keyPressed(KeyEvent e) {
        if (checkDeath()) {
            return;
        }
        boolean moved = false;
        Object o = null;
        if (e.getKeyCode() == Settings.INVENTORY) {
            toggleInventory();
        }
        if (e.getKeyCode() == Settings.REST && health < maxHealth) {
            moved = true;
            regenStepsCounter++;
            if (Helper.random.nextInt(99) + 1 < 15) {
                health++;
            }
        }
        if (!status.isParalyzed() && !status.isConfused() && !showInventory) {
            if(e.getKeyCode() == Settings.MOVE_DOWN) {
                moved = move(DOWN);
                if (!moved) {
                    hitMonster(DOWN);
                }
            } else if (e.getKeyCode() == Settings.MOVE_UP) {
                moved = move(UP);
                if (!moved) {
                    hitMonster(UP);
                }
            } else if (e.getKeyCode() == Settings.MOVE_RIGHT) {
                moved = move(RIGHT);
                if (!moved) {
                    hitMonster(RIGHT);
                }
            } else if (e.getKeyCode() == Settings.MOVE_LEFT) {
                moved = move(LEFT);
                if (!moved) {
                    hitMonster(LEFT);
                }
            } else if (e.getKeyCode() == Settings.USE_STAIRCASE) {
                if (overWrittenGraphic.equals("%")) {
                    changeLevel(Level.getLevel().getStaircase().getDirection());
                }
            } else if (e.getKeyCode() == Settings.SEARCH) {
                moved = true;
                o = search();
            }
        }
        if (status.isConfused() || status.isDrunk()) {
            moved = moveRandom();
        }
        if (moved || getStatus().isParalyzed()) {
            map.update();
            if (o != null) {
                if (o instanceof Door) {
                    ((Door) o).reveal();
                } else if (o instanceof Trap) {
                    ((Trap) o).reveal();
                }
            }
        }
        if (moved && health < maxHealth) {
            regenStepsCounter++;
            if (regenStepsCounter >= 21 - level) {
                health++;
                regenStepsCounter = 0;
                Map.getMap().getStatusBar().updateStatusBar();
            }
            if (getStatus().isPoisoned() && health > 3) {
                health--;
            }
        }
        status.update();
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    // KEY LISTENER HELPER METHODS

    private Object search() {
        for (Door door : Door.getDoors()) {
            if (door.isSecret() && isNextTo(door)) {
                int chance = Helper.random.nextInt(99) + 1;
                if (getStatus().getEffects().hasEffect(Effect.SEARCHING) && chance <= 90) {
                    return door;
                } else if (chance <= 75){
                    return door;
                }
            }
        }
        for (Trap trap : Trap.getTraps()) {
            if (trap.isHidden() && isNextTo(new Point(trap.getXPos(), trap.getYPos()))) {
                return trap;
            }
        }
        return null;
    }
    private boolean isNextTo(Point p) {
        return
                ((p.getX() + 1 == getXPos() || p.getX() - 1 == getXPos()) && p.getY() == getYPos()) ||
                        ((p.getY() + 1 == getYPos() || p.getY() - 1 == getYPos()) && p.getX() == getXPos());
    }
    public void toggleInventory() {
        showInventory = !showInventory;
        if (showInventory) {
            savedContentPane = GameManager.getFrame().getContentPane();
            new InventoryPane();
        } else {
            GameManager.replaceContentPane((JPanel) savedContentPane);
        }
    }
    public void toggleInventory(String message) {
        showInventory = !showInventory;
        savedContentPane = GameManager.getFrame().getContentPane();
        new InventoryPane(message, new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    for (int i = 0; i < InventoryItem.getInventoryItems().size(); i++) {
                        InventoryItem item = InventoryItem.getInventoryItems().get(i);
                        if (item.getPanel().getBounds().contains(e.getPoint())) {
                            GameManager.getPlayer().toggleInventory();
                            item.getItem().identify();
                        }
                    }
                }
            }
        });
    }
    private void hitMonster(int direction) {
        for (int i = 0; i < Monster.getMonsters().size(); i++) {
            Monster monster = Monster.getMonsters().get(i);
            if (fakeMove(direction).getX() == monster.getXPos() && fakeMove(direction).getY() == monster.getYPos()) {
                double hitChance = (100 - ((10 - monster.getStatus().getAc()) * 3) + 30) / 100;
                if (Helper.random.nextDouble() <= hitChance) {
                    Status monsterStatus = monster.getStatus();
                    monsterStatus.lowerHealth(getDamage());
                    map.update();
                    if (monster.getStatus().getHealth() > 0) {
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
        Map.getMap().getStatusBar().updateStatusBar();
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
        if (!checkTrap()) {
            checkToPickUpItem();
        }
        if (getStatus().getEffects().hasEffect(Effect.REGENERATION) && health < maxHealth) {
            health++;
        }
    }
    private int updateHungerStatus() {
        if (stepsTakenSinceMeal > (getStatus().getEffects().hasEffect(Effect.SLOW_DIGESTION) ? 400 : 200)) {
            if (stepsTakenSinceMeal > (getStatus().getEffects().hasEffect(Effect.SLOW_DIGESTION) ? 800 : 400)) {
                if (stepsTakenSinceMeal > (getStatus().getEffects().hasEffect(Effect.SLOW_DIGESTION) ? 1600 : 800)) {
                    if (stepsTakenSinceMeal > (getStatus().getEffects().hasEffect(Effect.SLOW_DIGESTION) ? 3200 : 1600)) {
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
    private boolean checkTrap() {
        for (Trap trap : Trap.getTraps()) {
            if (getXPos() == trap.getXPos() && getYPos() == trap.getYPos()) {
                trap.trigger();
                return true;
            }
        }
        return false;
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
                overWrittenGraphic.equals("/") || overWrittenGraphic.equals("!") ||
                overWrittenGraphic.equals(")") || overWrittenGraphic.equals(",")) {
            Item foundItem = Item.getItemAt(getXPos(), getYPos());
            if (foundItem == null) {
                return;
            }

            overWrittenGraphic = foundItem.overWrittenGraphic;

            inventory.add(foundItem);
            if (foundItem instanceof Food) {
                MessageBar.addMessage("You found some " + foundItem.getName().toLowerCase());
            } else if (foundItem instanceof Armor || foundItem instanceof Arrow) {
                MessageBar.addMessage("You found " + foundItem.getName());
            } else if (foundItem.getName().startsWith("A") || foundItem.getName().startsWith("E") ||
                    foundItem.getName().startsWith("O") || foundItem.getName().startsWith("I") ||
                    foundItem.getName().startsWith("U")) {
                MessageBar.addMessage("You found an " + foundItem.getName());
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
        int damage = 2;
        if (getHeldItem() != null) {
            damage = ((Weapon) getHeldItem()).getDamage();
        }
        damage = getStatus().isWeakened() ?  damage - 1 : damage;
        damage = getStatus().getEffects().hasEffect(Effect.STRENGTH) ? damage + 2 : damage;
        if (getStatus().isPoisoned()) {
            damage -= Helper.random.nextInt(1) + 1;
        }
        if (getStatus().isStrengthened()) {
            damage += 1;
        }
        return damage;
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
    public Armor getWornItem() {
        return wornItem;
    }
    public ArrayList<Ring> getRings() {
        ArrayList<Ring> rings = new ArrayList<>();
        if (leftRing != null) {
            rings.add(leftRing);
        }
        if (rightRing != null) {
            rings.add(rightRing);
        }
        return rings;
    }
    public int getLevel() {
        return level;
    }

    // SETTERS

    public void addExperience(int experience) {
        this.experience += experience;
    }
    public int stealGold(int amount) {
        if (gold - amount >= 0) {
            this.gold -= amount;
        } else {
            amount = this.gold;
            this.gold = 0;
        }
        return amount;
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
        Map.getMap().getStatusBar().updateStatusBar();
    }
    public void setWornItem(Armor wornItem) {
        this.wornItem = wornItem;
        if (wornItem != null) {
            getStatus().setAc(wornItem.getAc());
        } else {
            getStatus().setAc(9);
        }
        getStatus().getEffects().removeEffect(Effect.PROTECT_ARMOR);
        Map.getMap().getStatusBar().updateStatusBar();
    }
    public void setHeldItem(Item heldItem) {
        this.heldItem = heldItem;
    }
    public void wearRing(Ring ring) {
        if (leftRing == null) {
            leftRing = ring;
        } else if (rightRing == null) {
            rightRing = ring;
        } else {
            // TODO: figure out what to do if both hands already have rings (how to replace an already worn ring)
        }
    }
    public void setOverwrittenGraphic(String overwrittenGraphic) {
        this.overWrittenGraphic = overwrittenGraphic;
        GameManager.getTable().setValueAt(graphic, getYPos(), getXPos());
    }
}
