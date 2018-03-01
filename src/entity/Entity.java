package entity;

import main.GameManager;

public class Entity {
    int xPos;
    int yPos;

    char graphic;

    char overWrittenGraphic = '_';

    static int UP = 0;
    static int DOWN = 1;
    static int RIGHT = 2;
    static int LEFT = 3;

    public Entity(char g, int x, int y) {
        xPos = x;
        yPos = y;
        graphic = g;

        GameManager.add(graphic, x, y);
    }
    public void move(int direction) {
        GameManager.add(overWrittenGraphic, xPos, yPos);
        if (direction == UP) {
            yPos += 1;
        } else if (direction == DOWN) {
            yPos -= 1;
        } else if (direction == RIGHT) {
            xPos += 1;
        } else if (direction == LEFT) {
            xPos -= 1;
        }
        GameManager.add(graphic, xPos, yPos);
    }
}
