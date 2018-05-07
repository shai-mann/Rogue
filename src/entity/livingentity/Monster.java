package entity.livingentity;

import entity.Entity;
import entity.Status;
import entity.item.Gold;
import entity.item.Item;
import extra.MessageBar;
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

    private enum movementTypes { WANDER, TRACK, MIMIC, STILL, GOLD_TRACK, SLEEP }
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

    public String name = "<Default>";
    private int speed = 1;
    private int moveCounter = 1;
    private int range = 10;
    private double hitChance = 0.5;
    private int hitDamage = 1;
    private double critChance = 0.05;
    private double critDamage = hitDamage * 2;
    private boolean invisible = false;
    private int experience = 2;
    private double treasureChance = 0.3;
    private boolean startSleeping = true;

    private Item stolenItem = null;

    private Status status;

    private movementTypes movementType = movementTypes.TRACK;
    private attackTypes attackType = attackTypes.HIT;

    public Monster(String dataFilePath, int x, int y) {
        super("-", x, y);
        monsters.add(this);
        status = new Status();
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
            case "hitchance":
                this.hitChance = Double.parseDouble(parsed[1]);
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
                startSleeping = true;
                return movementTypes.SLEEP;
            case "mimic":
                startSleeping = true;
                return movementTypes.MIMIC;
            case "goldtracking":
            case "gold tracking":
                return movementTypes.GOLD_TRACK;
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
            default:
                return attackTypes.HIT; // default attack type
        }
    }

    // MONSTER BEHAVIOR

    private void runUpdate() {
        // TODO: Add rust armor, trap attack,
        // TODO: steal gold, steal (magic) item, weakness, drain max HP, drain XP (levels loss if go below thresh-hold), mimic (imitate object)
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
            case GOLD_TRACK:
                goldTrackMovement();
                break;
            case SLEEP:
                sleepMovement();
                break;
            case MIMIC:
                mimicMovement();
                break;
        }
        status.update();
        if (status.getHealth() <= 0) {
            this.die();
        }
    }

    // MONSTER MOVEMENT

    private void trackMovement() {
        Player player = GameManager.getPlayer();

        if (moveCounter == speed) {
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
                    moveCounter = 1;
                } else {
                    attack();
                }
            } else {
                moveRandom();
            }
        } else {
            moveCounter += 1;
        }
    }
    private void wanderMovement() {
        if (isNextTo(GameManager.getPlayer())) {
            attack();
        }
        moveRandom();
    }
    private void stillMovement() {
        if (isNextTo(GameManager.getPlayer())) {
            attack();
        }
    }
    private void goldTrackMovement() {
        Gold gold = (Gold) Item.getClosestItem(this, Item.getAllItemsOfType(Item.itemTypes.GOLD));
        if (moveCounter == speed) {
            if (isInRange(gold)) {
                if (!isNextTo(gold)) {
                    if (this.getYPos() > gold.getYPos()) {
                        move(UP);
                    } else {
                        move(DOWN);
                    }
                    if (this.getXPos() < gold.getXPos()) {
                        move(RIGHT);
                    } else {
                        move(LEFT);
                    }
                    moveCounter = 1;
                }
            } else if (isInRange(GameManager.getPlayer())){
                trackMovement();
            } else {
                moveRandom();
            }
        } else {
            moveCounter += 1;
        }
    }
    private void sleepMovement() {
        if (startSleeping) {
            getStatus().setSleeping(1);
        } else {
            trackMovement();
        }
    }
    private void mimicMovement() {
        if (startSleeping) {
            this.getStatus().setSleeping(1);
            this.overWrittenGraphic = graphic;

            String[] chars = {"\\", "&", "*", "^", "]"};
            graphic = (String) Helper.getRandom((ArrayList<String>) Arrays.asList(chars));
        }
    }

    // MONSTER ATTACK

    private void attack() {
        switch (attackType) {
            case HIT:
                hitAttack();
                break;
            case PARALYZE:
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
        }
    }
    private void hitAttack() {
        if (Helper.random.nextDouble() <= hitChance) {
            if (Helper.random.nextDouble() <= critChance) { // critical
                if (Helper.random.nextDouble() <= 0.8) {
                    GameManager.getPlayer().health -= critDamage;
                    MessageBar.addMessage("The " + this.name + " crits");
                } else {
                    GameManager.getPlayer().health -= hitDamage;
                    GameManager.getPlayer().getStatus().setParalyzed(3);
                }
            } else {
                GameManager.getPlayer().health -= hitDamage;
                MessageBar.addMessage("The " + this.name + " hits");
            }
        } else {
            MessageBar.addMessage("The " + this.name + " misses");
        }
    }
    private void paralyzeAttack() {
        Player player = GameManager.getPlayer();
        if (!player.getStatus().isParalyzed()) {
            GameManager.getPlayer().getStatus().setParalyzed(3);
        }
        MessageBar.addMessage("You are paralyzed");
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
        GameManager.getPlayer().getStatus().setWeakened(Helper.random.nextInt(10) + 2);
        MessageBar.addMessage("You feel weaker");
    }
    private void stealGoldAttack() {
        GameManager.getPlayer().stealGold(Helper.random.nextInt(50) + GameManager.getPlayer().getGold() / 4);
        MessageBar.addMessage("Your pockets feel lighter");
    }
    private void stealItemAttack() {
        ArrayList<Item> items = (ArrayList<Item>) GameManager.getPlayer().getInventory().clone();
        Item.itemTypes[] itemTypes = {Item.itemTypes.RING, Item.itemTypes.WAND, Item.itemTypes.STAFF};

        items = Item.getAllItemsOfTypes((ArrayList<Item.itemTypes>) Arrays.asList(itemTypes), items);

        stolenItem = (Item) Helper.getRandom(items);
        GameManager.getPlayer().getInventory().remove(stolenItem);
        MessageBar.addMessage("Your backpack feels lighter");
    }

    // OVERRIDES

    public boolean move(int direction) {
        super.move(direction);
        if (this.invisible) {
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
        int amount = Integer.parseInt(parts[0]);
        int faces = Integer.parseInt(parts[1]);
        int total = 0;
        for (int i = 0; i < amount; i++) {
            total += new Random().nextInt(faces) + 1;
        }
        return total;
    }

    private void moveRandom() {
        int[] directions = {UP,DOWN,RIGHT,LEFT};
        move(directions[new Random().nextInt(directions.length)]);
        moveCounter = 1;
    }
    public void die() {
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

    // GETTERS

    public Status getStatus() {
        return status;
    }
    public String getName() { return name; }

    // MONSTER SPAWNING METHODS

    private static void createMonster(Room room) {
        Point location = room.getRandomPointInBounds();
        new Monster(((File) Helper.getRandom(availableFiles)).getPath(), location.x, location.y);
    }
    public static void spawnMonsters() {
        // TODO: Update to use Poisson's thingy
        createMonster(Level.getLevel().getStartingRoom());
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
        files = new File("data/monsters/").listFiles();
    }
    public static void update() {
        for (int i = 0; i < monsters.size(); i++) {
            Monster monster = monsters.get(i);
            monster.runUpdate();
        }
    }
    public static ArrayList<Monster> getMonsters() {
        return monsters;
    }
}