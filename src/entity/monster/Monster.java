package entity.monster;

import entity.Entity;
import entity.Player;
import entity.Status;
import main.GameManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Monster extends Entity {

    static ArrayList<Monster> monsters = new ArrayList<>();

    public static int DEFAULT_HEALTH = 20;

    private enum movementTypes { WANDER, TRACK }
    private enum attackTypes { HIT, SHOOT, PARALYZE, CONFUSE, INTOXICATE }

    public String name = "<Default>";
    private int speed = 1;
    private int moveCounter = 1;
    private int range = 10;
    private double hitChance = 0.5;
    private int hitDamage = 1;
    private double critChance;

    private Status status;

    private movementTypes movementType = movementTypes.TRACK;
    private attackTypes attackType = attackTypes.HIT;

    public Monster(String dataFilePath, int x, int y) {
        super("_", x, y);
        monsters.add(this);
        loadDataFile(dataFilePath);
        status = new Status();
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
        switch (parsed[0]) {
            case "name":
                name = parsed[1];
                break;
            case "graphic":
                this.graphic = parsed[1];
                GameManager.add(graphic, getXPos(), getYPos());
                break;
            case "hitChance":
                this.hitChance = Double.parseDouble(parsed[1]);
                break;
            case "critChance":
                this.critChance = Double.parseDouble(parsed[1]);
                break;
            case "hitDamage":
                this.hitDamage = Integer.parseInt(parsed[1]);
                break;
            case "speed":
                this.speed = Integer.parseInt(parsed[1]);
                break;
            case "range":
                this.range = Integer.parseInt(parsed[1]);
                break;
            case "health":
                this.health = Integer.parseInt(parsed[1]);
                break;
            case "movementType":
                this.movementType = movementTypeFromString(parsed[1]);
                break;
            case "attackType":
                this.attackType = attackTypeFromString(parsed[1]);
                break;
        }
    }

    private movementTypes movementTypeFromString(String string) {
        switch (string) {
            case "wander":
                return movementTypes.WANDER;
            case "track":
                return movementTypes.TRACK;
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
        switch (movementType) {
            case TRACK:
                trackMovement();
                break;
            case WANDER:
                wanderMovement();
                break;
        }
        status.update();
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
        Random random = new Random();
        if (random.nextDouble() <= hitChance) {
            GameManager.getPlayer().health -= hitDamage;
            if (random.nextDouble() <= critChance) { // critical
                if (random.nextDouble() <= 0.8) {
                    GameManager.getPlayer().health -= hitDamage;
                } else {
                    // paralyze
                }
            }
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
        GameManager.getPlayer().getStatus().setConfused(3);
    }
    private void intoxicateAttack() {
        GameManager.getPlayer().getStatus().setDrunk(3);
    }

    // HELPERS

    private boolean isInRange(Player player) {
        return Math.pow(player.getXPos() - getXPos(), 2) + Math.pow(player.getYPos() - getYPos(), 2) < Math.pow(range + 1, 2);
    }
    private boolean isNextTo(Player player) {
        return
                ((player.getXPos() + 1 == getXPos() || player.getXPos() - 1 == getXPos()) && player.getYPos() == getYPos()) ||
                        ((player.getYPos() + 1 == getYPos() || player.getYPos() - 1 == getYPos()) && player.getXPos() == getXPos());
    }

    private void moveRandom() {
        int[] directions = {UP,DOWN,RIGHT,LEFT};
        move(directions[new Random().nextInt(directions.length)]);
        moveCounter = 1;
    }

    // STATIC METHODS

    public static void update() {
        // run through monster list and update positions
        for (Monster monster : monsters) {
            monster.runUpdate();
        }
        // has to call some method in map that runs the statusBar.updateStatusBar();
    }

}





