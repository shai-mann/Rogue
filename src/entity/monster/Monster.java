package entity.monster;

import entity.Entity;
import entity.Player;
import main.GameManager;

import java.util.ArrayList;

public class Monster extends Entity {

    static ArrayList<Monster> monsters = new ArrayList<Monster>();

    int speed = 1;
    int moveCounter = 1;

    public Monster(String s, int x, int y) {
        super(s, x, y);
        monsters.add(this);
    }
    public void runUpdate() {
        Player player = GameManager.getPlayer();

        if (player.getXPos() - getXPos() != 0 && player.getYPos() - getYPos() != 0) {
            if (moveCounter == speed) {
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
            }
            else {
                moveCounter += 1;
            }
        } else {
            hit();
        }
    }
    public static void update() {
        // run through monster list and update positions
        for (Monster monster : monsters) {
            monster.runUpdate();
        }
    }
    public void hit() {
        System.out.println("ez bangers ur ded");
    }
}
