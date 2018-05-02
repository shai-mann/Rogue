package entity.monster;

import entity.Entity;
import entity.Player;
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
import java.util.Random;

public class Monster extends Entity {

    static ArrayList<Monster> monsters = new ArrayList<>();
    static File[] files;
    static ArrayList<File> availableFiles = new ArrayList<>();

    public static int DEFAULT_HEALTH = 20;

    private enum movementTypes { WANDER, TRACK, MIMIC, STILL, GOLD_TRACK, SLEEP }
    private enum attackTypes {
        HIT,
        SHOOT,
        PARALYZE,
        CONFUSE,
        INTOXICATE,
        WEAKEN,
        DRAIN,
        RUST,
        RANGE,
        TRAP,
        STEAL
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
    private double treasureDropChance = 0.3;
    // TODO: implement treasure dropping and experience gaining

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
        // todo: error handling if data is misformatted
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
                this.critDamage = Double.parseDouble(parsed[1]);
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
            case "treasure":
                this.treasureDropChance = Integer.parseInt(parsed[1]) / 100;
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
                return movementTypes.SLEEP;
            case "mimic":
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
            case "shoot":
                return attackTypes.SHOOT;
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
        // TODO: Add rust armor, range attack, trap attack, steal gold, steal (magic) item, pathfind gold, weakness, drain max HP, mimic (immitate object)
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
        }
        status.update();
        if (status.getHealth() <= 0) {
            this.die();
        }
    }
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
    private void attack() {
        switch (attackType) {
            case HIT:
                hitAttack();
                break;
            case SHOOT:
                shootAttack();
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
    private void shootAttack() {
        // TODO: create shoot attack
    }
    private void paralyzeAttack() {
        Player player = GameManager.getPlayer();
        if (!player.getStatus().isParalyzed()) {
            GameManager.getPlayer().getStatus().setParalyzed(3);
        }
    }
    private void confuseAttack() {
        int ticks = new Random().nextInt(11) + 10; // random number between 10 and 20
        GameManager.getPlayer().getStatus().setConfused(ticks);
    }
    private void intoxicateAttack() {
        // TODO: delay between two steps
        GameManager.getPlayer().getStatus().setDrunk(3);
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
    private void die() {
        GameManager.add(overWrittenGraphic, getXPos(), getYPos());
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
        createMonster(Level.getLevel().getStartingRoom());
        createMonster((Room) Helper.getRandom(Room.rooms));
        // TODO: fix to use Poisson's method for spawning monsters
    }

    // STATIC METHODS

    private static void updateAvailableMonsters() {
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
        updateAvailableMonsters();
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