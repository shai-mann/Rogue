package entity;

import main.GameManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Player extends Entity implements KeyListener {

    public Player() {
        super("@", 5, 5);
        GameManager.getFrame().addKeyListener(this);
    }

    public void keyPressed(KeyEvent e) {
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
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
