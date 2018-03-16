package entity.monster;

import entity.Entity;
import entity.Player;
import main.GameManager;
import map.StatusBar;

import java.util.ArrayList;
import java.util.Random;

public class Monster extends Entity {

    static ArrayList<Monster> monsters = new ArrayList<>();

    public static int DEFAULT_HEALTH = 20;

    int speed = 1;
    int moveCounter = 1;
    int range = 10;
    double hitChance = 0;
    int hitDamage = 0;

    public Monster(String s, int x, int y) {
        super(s, x, y);
        monsters.add(this);
    }
    public void runUpdate() {
        Player player = GameManager.getPlayer();

        if (moveCounter == speed) {
            if (!isNextTo(player) && isInRange()) {
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
                hit();
            }
        } else {
            moveCounter += 1;
        }
    }
    public static void update() {
        // run through monster list and update positions
        for (Monster monster : monsters) {
            monster.runUpdate();
        }
        // has to call some method in map that runs the statusBar.updateStatusBar();
    }
    public boolean isInRange() {
        return true;
    }
    public boolean isNextTo(Player player) {
        return
            ((player.getXPos() + 1 == getXPos() || player.getXPos() - 1 == getXPos()) && player.getYPos() == getYPos()) ||
            ((player.getYPos() + 1 == getYPos() || player.getYPos() - 1 == getYPos()) && player.getXPos() == getXPos());
    }
    public void hit() {
        Random random = new Random();
        if (random.nextDouble() <= hitChance) {
            GameManager.getPlayer().health -= hitDamage;
            if (random.nextDouble() <= 0.1) { // critical
                if (random.nextDouble() <= 0.8) {
                    GameManager.getPlayer().health -= hitDamage;
                } else {
                    // paralyze
                }
            }
        }
    }
}
