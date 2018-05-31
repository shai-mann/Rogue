package entity.livingentity;

import com.sun.istack.internal.Nullable;
import entity.Effect;
import entity.Entity;
import entity.Status;
import entity.lifelessentity.item.Item;
import map.level.Passageway;
import util.MessageBar;
import helper.Helper;
import main.GameManager;
import map.level.Level;
import map.level.Room;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Monster extends Entity {

    private static ArrayList<Monster> monsters = new ArrayList<>();
    private static File[] files;
    private static ArrayList<File> availableFiles = new ArrayList<>();

    public static int DEFAULT_HEALTH = 20;

    private enum movementTypes { WANDER, TRACK, MIMIC, STILL, SLEEP }
    private enum attackTypes {
        HIT,
        PARALYZE,
        CONFUSE,
        INTOXICATE,
        WEAKEN,
        HP_DRAIN,
        XP_DRAIN,
        RUST,
        TRAP,
        STEAL_GOLD,
        STEAL_ITEM
    }

    private int speed = 1;
    private int moveCounter = 1;
    private int range = 10;
    private int hitDamage = 1;
    private double critChance = 0.05;
    private double critDamage = hitDamage * 2;
    private boolean invisible = false;
    private int experience = 2;
    private double treasureChance = 0.3;
    private String hiddenChar;

    private Item stolenItem = null;

    private Status status;

    private movementTypes movementType = movementTypes.TRACK;
    private movementTypes secondaryMovementType = movementTypes.WANDER;
    // Only wander and track can be secondary movement types.
    private attackTypes attackType = attackTypes.HIT;

    public Monster(String dataFilePath, int x, int y) {
        super("-", x, y);
        monsters.add(this);
        status = new Status(this);
        loadDataFile(dataFilePath);
    }

    // DATA LOADING

    private void loadDataFile(String path) {

        String line;
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                applyAttributeFromLine(line);
            }

            bufferedReader.close();

        } catch (FileNotFoundException e) {
            // TODO: Better error handling
            System.out.println("Error: file not found");
        } catch (IOException e) {
            System.out.println("Error reading file");
        }
    }
    private void applyAttributeFromLine(String line) {
        String[] parsed = line.split(":");
        switch (parsed[0].toLowerCase()) {
            case "name":
                name = parsed[1];
                break;
            case "graphic":
                this.graphic = parsed[1];
                GameManager.add(graphic, getXPos(), getYPos());
                break;
            case "critchance":
                this.critChance = Double.parseDouble(parsed[1]);
                break;
            case "hitdamage":
                this.hitDamage = parseDiceNotation(parsed[1]);
                break;
            case "critdamage":
                this.critDamage = parseDiceNotation(parsed[1]);
                break;
            case "speed":
                this.speed = Integer.parseInt(parsed[1]);
                break;
            case "range":
                this.range = Integer.parseInt(parsed[1]);
                break;
            case "health":
                this.health = parseDiceNotation(parsed[1]);
                break;
            case "movementtype":
                this.movementType = movementTypeFromString(parsed[1]);
                break;
            case "secondarymovementtype":
                this.secondaryMovementType = movementTypeFromString(parsed[1]);
                break;
            case "attacktype":
                this.attackType = attackTypeFromString(parsed[1]);
                break;
            case "invisible":
                this.invisible = Boolean.valueOf(parsed[1]);
                break;
            case "ac":
                this.status.setAc(Integer.valueOf(parsed[1]));
                break;
            case "experience":
                this.experience = Integer.parseInt(parsed[1]);
                break;
            case "treasure":
                this.treasureChance = Integer.parseInt(parsed[1]) / 100;
                break;
        }
    }

    // DATA PARSING HELPER METHODS

    private movementTypes movementTypeFromString(String string) {
        switch (string.toLowerCase().trim()) {
            case "wander":
                return movementTypes.WANDER;
            case "track":
                return movementTypes.TRACK;
            case "sleep":
                getStatus().setSleeping(true);
                return movementTypes.SLEEP;
            case "mimic":
                getStatus().setSleeping(true);
                hiddenChar = graphic;
                String[] chars = {"\\", "&", "*", "]", "%", "?", "!", "(", ","};
                graphic = (String) Helper.getRandom(new ArrayList(Arrays.asList(chars)));
                GameManager.getTable().setValueAt(graphic, getYPos(), getXPos());
                return movementTypes.MIMIC;
            case "still":
                return movementTypes.STILL;
            default:
                return movementTypes.TRACK; // default movement type
        }
    }
    private attackTypes attackTypeFromString(String string) {
        switch (string) {
            case "attack":
                return attackTypes.HIT;
            case "paralyze":
                return attackTypes.PARALYZE;
            case "confuse":
                return attackTypes.CONFUSE;
            case "intoxicate":
                return attackTypes.INTOXICATE;
            case "weaken":
                return attackTypes.WEAKEN;
            case "hpdrain":
            case "hp_drain":
                return attackTypes.HP_DRAIN;
            case "xpdrain":
            case "xp_drain":
                return attackTypes.XP_DRAIN;
            case "rust":
                return attackTypes.RUST;
            case "trap":
                return attackTypes.TRAP;
            case "stealgold":
            case "steal_gold":
                return attackTypes.STEAL_GOLD;
            case "stealitem":
            case "steal_item":
                return attackTypes.STEAL_ITEM;
            default:
                return attackTypes.HIT; // default attack type
        }
    }

    // MONSTER BEHAVIOR

    private void runUpdate() {
        // TODO: steal (magic) item, drain max HP, drain XP (levels loss if go below thresh-hold)
        move(null);
        status.update();
        if (status.getHealth() <= 0) {
            this.die();
        }
    }

    // MONSTER MOVEMENT

    private void move(@Nullable movementTypes type) {
        if (speed > 0 && !getStatus().getEffects().hasEffect(Effect.SUPPRESS_POWER)) {
            for (int i = 0; i < speed; i++) {
                moveHelper(type);
            }
        } else {
            if (moveCounter == -speed) {
                moveHelper(type);
                moveCounter = 1;
            } else {
                moveCounter += 1;
            }
        }
    }
    private void moveHelper(@Nullable movementTypes type) {
        if (type != null) {
            switch (type) {
                case TRACK:
                    trackMovement();
                    break;
                case WANDER:
                    wanderMovement();
                    break;
                case STILL:
                    stillMovement();
                    break;
                case SLEEP:
                    sleepMovement();
                    break;
                case MIMIC:
                    mimicMovement();
                    break;
            }
            return;
        }
        switch (movementType) {
            case TRACK:
                trackMovement();
                break;
            case WANDER:
                wanderMovement();
                break;
            case STILL:
                stillMovement();
                break;
            case SLEEP:
                sleepMovement();
                break;
            case MIMIC:
                mimicMovement();
                break;
        }
    }
    private void trackMovement() {
        Player player = GameManager.getPlayer();

        if (isInRange(player)) {
            if (!isNextTo(player)) {
                if (this.getYPos() > player.getYPos()) {
                    move(UP);
                } else {
                    move(DOWN);
                }
                if (this.getXPos() < player.getXPos()) {
                    move(RIGHT);
                } else {
                    move(LEFT);
                }
            } else {
                attack();
            }
        } else {
            moveRandom();
        }


    }
    private void wanderMovement() {
        if (isNextTo(GameManager.getPlayer()) && !(Helper.random.nextInt(99) + 1 <= 90)) {
            attack();
        } else {
            moveRandom();
        }
    }
    private void stillMovement() {
        if (isNextTo(GameManager.getPlayer())) {
            attack();
        }
    }
    private void sleepMovement() {
        if (!getStatus().isSleeping() || GameManager.getPlayer().getStatus().getEffects().hasEffect(Effect.AGGRAVATE_MONSTER)) {
            move(secondaryMovementType);
        }
    }
    private void mimicMovement() {
        if (!getStatus().isSleeping()) {
            move(secondaryMovementType);
            GameManager.getTable().setValueAt(hiddenChar, getYPos(), getXPos());
        } else {
            GameManager.getTable().setValueAt(graphic, getYPos(), getXPos());
        }
    }

    // MONSTER ATTACK

    private void attack() {
        if (Helper.random.nextDouble() <= (double) GameManager.getPlayer().getStatus().getAc() / 10) {
            if (!getStatus().getEffects().hasEffect(Effect.SUPPRESS_POWER)) {
                switch (attackType) {
                    case HIT:
                        hitAttack();
                        break;
                    case PARALYZE:
                    case TRAP:
                        paralyzeAttack();
                        break;
                    case CONFUSE:
                        confuseAttack();
                        break;
                    case INTOXICATE:
                        intoxicateAttack();
                        break;
                    case WEAKEN:
                        weakenAttack();
                        break;
                    case STEAL_GOLD:
                        stealGoldAttack();
                        break;
                    case STEAL_ITEM:
                        stealItemAttack();
                        break;
                    case RUST:
                        rustAttack();
                        break;
                    case XP_DRAIN:
                        xpDrainAttack();
                        break;
                    case HP_DRAIN:
                        healthDrainAttack();
                        break;
                }
            } else {
                hitAttack();
            }
        } else {
            MessageBar.addMessage("The " + getName() + " misses");
        }
    }
    private void hitAttack() {
        if (Helper.random.nextDouble() <= critChance) { // critical
            if (Helper.random.nextDouble() <= 0.8) {
                GameManager.getPlayer().health -= Helper.random.nextInt((int) critDamage) + 1;
                MessageBar.addMessage("The " + getName() + " crits");
            } else {
                GameManager.getPlayer().health -= Helper.random.nextInt(hitDamage) + 1;
                GameManager.getPlayer().getStatus().setParalyzed(3);
            }
        } else {
            GameManager.getPlayer().health -= Helper.random.nextInt(hitDamage) + 1;
            MessageBar.addMessage("The " + getName() + " hits");
        }
    }
    private void paralyzeAttack() {
        Player player = GameManager.getPlayer();
        if (!player.getStatus().isParalyzed()) {
            GameManager.getPlayer().getStatus().setParalyzed(3);
        }
        MessageBar.addMessage("You can't move");
    }
    private void confuseAttack() {
        GameManager.getPlayer().getStatus().setConfused(Helper.random.nextInt(11) + 10);
        MessageBar.addMessage("You feel confused");
    }
    private void intoxicateAttack() {
        // TODO: delay between two steps
        GameManager.getPlayer().getStatus().setDrunk(3);
        MessageBar.addMessage("You feel strange");
    }
    private void weakenAttack() {
        if (GameManager.getPlayer().getStatus().getEffects().hasEffect(Effect.SUSTAIN_STRENGTH)) {
            MessageBar.addMessage("The " + getName() + "'s attack was magically deflected");
        } else {
            GameManager.getPlayer().getStatus().setWeakened(Helper.random.nextInt(10) + 2);
            MessageBar.addMessage("You feel weaker");
        }
    }
    private void stealGoldAttack() {
        GameManager.getPlayer().stealGold(Helper.random.nextInt(50) + GameManager.getPlayer().getGold() / 4);
        MessageBar.addMessage("Your pockets feel lighter");
    }
    private void stealItemAttack() {
        ArrayList<Item> items = (ArrayList<Item>) GameManager.getPlayer().getInventory().clone();
        Item.itemTypes[] itemTypes = {Item.itemTypes.RING, Item.itemTypes.WAND, Item.itemTypes.POTION, Item.itemTypes.SCROLL};

        items = Item.getAllItemsOfTypes((ArrayList<Item.itemTypes>) Arrays.asList(itemTypes), items);

        stolenItem = (Item) Helper.getRandom(items);
        GameManager.getPlayer().getInventory().remove(stolenItem);
        MessageBar.addMessage("Your backpack feels lighter");
    }
    private void rustAttack() {
        if (GameManager.getPlayer().getWornItem() != null &&
                !GameManager.getPlayer().getStatus().getEffects().hasEffect(Effect.PROTECT_ARMOR)) {
            GameManager.getPlayer().getStatus().setAc(GameManager.getPlayer().getStatus().getAc() + 1);

            MessageBar.addMessage("Your armor feels worse");
        } else if (GameManager.getPlayer().getStatus().getEffects().hasEffect(Effect.PROTECT_ARMOR)) {
            MessageBar.addMessage("The " + getName() + "'s attack was magically deflected");
        }
    }
    private void xpDrainAttack() {
        GameManager.getPlayer().addExperience((int) -(GameManager.getPlayer().getExperience() * 0.9));
        MessageBar.addMessage("The " + name + " preys on your mind. Your experience drains away");
    }
    private void healthDrainAttack() {
        GameManager.getPlayer().drainMaxHealth((int) (GameManager.getPlayer().getMaxHealth() * 0.9));
        MessageBar.addMessage("");
    }

    // OVERRIDES

    public boolean move(int direction) {
        super.move(direction);
        if (this.invisible && !GameManager.getPlayer().getStatus().getEffects().hasEffect(Effect.SEE_INVISIBLE)) {
            GameManager.add(overWrittenGraphic, getXPos(), getYPos());
        }
        return true;
    }

    // HELPERS

    private boolean isInRange(Entity entity) {
        return Math.pow(entity.getXPos() - getXPos(), 2) + Math.pow(entity.getYPos() - getYPos(), 2) < Math.pow(range + 1, 2);
    }
    private boolean isNextTo(Player player) {
        return
                ((player.getXPos() + 1 == getXPos() || player.getXPos() - 1 == getXPos()) && player.getYPos() == getYPos()) ||
                        ((player.getYPos() + 1 == getYPos() || player.getYPos() - 1 == getYPos()) && player.getXPos() == getXPos());
    }
    private static int parseDiceNotation(String die) {
        String[] parts = die.split("d");
        return Integer.valueOf(parts[0]) * Integer.valueOf(parts[1]);
    }

    private void moveRandom() {
        int[] directions = {UP,DOWN,RIGHT,LEFT};
        move(directions[new Random().nextInt(directions.length)]);
        moveCounter = 1;
    }
    private void die() {
        GameManager.add(overWrittenGraphic, getXPos(), getYPos());
        GameManager.getPlayer().addExperience(experience);

        if (Helper.random.nextInt(99) + 1 < treasureChance) {
            if (stolenItem == null) {
                Item.spawnItem(getXPos(), getYPos(), null, null);
            } else {
                Item.spawnItem(getXPos(), getYPos(), null, stolenItem);
            }
        }

        MessageBar.addMessage("You kill the " + name);
        monsters.remove(this);
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }
    public void setType(File file) {
        loadDataFile(file.getPath());
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    // GETTERS

    public Status getStatus() {
        return status;
    }
    public String getHiddenChar() {
        return hiddenChar;
    }
    public int getSpeed() {
        return speed;
    }

    // MONSTER SPAWNING METHODS

    public static void createMonster(Room room) {
        Point location = room.getRandomPointInBounds();
        new Monster(((File) Helper.getRandom(availableFiles)).getPath(), location.x, location.y);
    }
    public static void spawnMonsters() {
        for (Room room : Room.rooms) {
            if (!room.equals(Level.getLevel().getStartingRoom())) {
                createMonster(room);
            }
        }
    }

    // STATIC METHODS

    public static void updateAvailableMonsters() {
        availableFiles.clear();
        for (File file : files) {
            try {
                if (getLevel(file).contains(Level.getLevel().getLevelNumber())) {
                    availableFiles.add(file);
                }
            } catch (NullPointerException e) {
                if (getLevel(file).contains(1)) {
                    availableFiles.add(file);
                }
            }
        }
    }
    private static ArrayList<Integer> getLevel(File file) {
        String line;
        ArrayList<Integer> validLevels = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("level")) {
                    line = line.split(":")[1];
                    String[] numbers = line.split(",");
                    for (String string : numbers) {
                        if (string.contains("-")) {
                            for (int i = Integer.parseInt(string.split("-")[0].trim());
                                 i <= Integer.parseInt(string.split("-")[1].trim()); i++) {
                                validLevels.add(i);
                            }
                        } else {
                            validLevels.add(Integer.parseInt(string.trim()));
                        }
                    }
                }
            }
            bufferedReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error: file not found");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading file");
        }
        return validLevels;
    }
    public static void loadCustomMonsters() {
        files = new File(Monster.class.getClassLoader().getResource("monsters").getPath()).listFiles();
    }
    public static void update() {
        for (int i = 0; i < monsters.size(); i++) {
            Monster monster = monsters.get(i);
            monster.runUpdate();
        }
    }
    public static Monster getClosestMonster(Entity e) {
        Monster closest = getMonsters().get(0);
        for (Monster m : getMonsters()) {
            if (Passageway.getDistance(m.getLocation(), e.getLocation()) < Passageway.getDistance(closest.getLocation(), e.getLocation())) {
                closest = m;
            }
        }
        return closest;
    }
    public static ArrayList<Monster> getMonsters() {
        return monsters;
    }
}