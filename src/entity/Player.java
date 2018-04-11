package entity;

import entity.monster.Monster;
import extra.GravePane;
import extra.MessageBar;
import main.GameManager;
import map.Map;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Player extends Entity implements KeyListener {

    private Map map;
    private Status status;

    public Player() {
        super("@", 1, 1);
        GameManager.getFrame().addKeyListener(this);
        status = new Status();
        status.setAc(8);
        map = Map.getMap();
    }

    public void keyPressed(KeyEvent e) {
        if (!status.isParalyzed() && !status.isConfused()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                case KeyEvent.VK_UP:
                    move(UP);
                    break;
                case KeyEvent.VK_S:
                case KeyEvent.VK_DOWN:
                    move(DOWN);
                    break;
                case KeyEvent.VK_A:
                case KeyEvent.VK_LEFT:
                    move(LEFT);
                    break;
                case KeyEvent.VK_D:
                case KeyEvent.VK_RIGHT:
                    move(RIGHT);
                    break;
                default:
                    break;
            }
        }
        if (status.isConfused() || status.isDrunk()) {
            int[] directions = {UP,DOWN,RIGHT,LEFT};
            move(directions[new Random().nextInt(directions.length)]);
        }
        map.update();
        Random rand = new Random();
        for (Monster monster: Monster.getMonsters()) {
            if (isNextTo(monster)) {
                double hitChance = (100 - ((10 - monster.getStatus().getAc()) * 3) + 30) / 100;
                if (rand.nextDouble() <= hitChance) {
                    Status monsterStatus = monster.getStatus();
                    monsterStatus.setHealth(monsterStatus.getHealth() - 1); // TODO: base this on sword
                    MessageBar.addMessage("You hit the " + monster.getName());
                }
            }
        }
        status.update();
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public int getHealth() {
        return health;
    }
    public void update() {
        if (checkDeath()) {
            new GravePane();
        }
    }
    public boolean checkValidMove(int direction) {
        String value = graphicAtMove(direction);

    }
    private boolean checkDeath() {
        return getHealth() == 0;
    }
    public Status getStatus() {
        return status;
    }
}
