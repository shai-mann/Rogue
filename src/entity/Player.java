package entity;

import entity.monster.Monster;
import extra.GravePane;
import main.GameManager;
import map.Map;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Player extends Entity implements KeyListener {

    private Map map;

    public Player() {
        super("@", 5, 5);
        GameManager.getFrame().addKeyListener(this);

        map = Map.getMap();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                move(UP);
                map.update();
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                move(DOWN);
                map.update();
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                move(LEFT);
                map.update();
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                move(RIGHT);
                map.update();
                break;
            default:
                break;
        }
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
    private boolean checkDeath() {
        return getHealth() == 0;
    }
}
