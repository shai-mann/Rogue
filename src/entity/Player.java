package entity;

import extra.GravePane;
import extra.MessageBar;
import main.GameManager;
import map.Map;
import map.level.Room;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Player extends Entity implements KeyListener {

    private Map map;
    private Status status;

    // TODO: Convert to Helper.random on merge

    public Player(Room room) {
        super("@", 0, 0);
        setLocation(room);
        GameManager.getFrame().addKeyListener(this);
        status = new Status();
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
        status.update();
        map.update();
                    MessageBar.addMessage("You hit the " + monster.getName());
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    private void setLocation(Room room) {
        // TODO: Change this to be Helper.random
        super.setLocation(room.getRandomPointInBounds());
    }
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
    public Status getStatus() {
        return status;
    }
}
