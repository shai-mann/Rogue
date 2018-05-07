package entity;

import entity.livingentity.Player;

import java.awt.*;

public class Staircase extends Entity {

    private int direction = Player.DOWN;

    public Staircase(Point p, int direction) {
        super("%", p.x, p.y);
        this.direction = direction;
    }
    public int getDirection() {
        return direction;
    }
}
