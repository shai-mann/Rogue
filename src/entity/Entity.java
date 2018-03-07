package entity;

import main.GameManager;

public class Entity {
    int xPos;
    int yPos;

    String graphic;

    String overWrittenGraphic = "-";

    static int UP = 0;
    static int DOWN = 1;
    static int RIGHT = 2;
    static int LEFT = 3;

    public Entity(String g, int x, int y) {
        xPos = x;
        yPos = y;
        graphic = g;

        GameManager.add(graphic, y, x);
    }
    public void move(int direction) {
        if (checkValidMove(direction)) {
            GameManager.add(overWrittenGraphic, yPos, xPos);
            if (direction == UP) {
                yPos -= 1;
            } else if (direction == DOWN) {
                yPos += 1;
            } else if (direction == RIGHT) {
                xPos += 1;
            } else if (direction == LEFT) {
                xPos -= 1;
            }

            overWrittenGraphic = (String) GameManager.getTable().getValueAt(yPos, xPos);

            GameManager.add(graphic, yPos, xPos);
        }
    }
    private boolean checkValidMove(int direction) {
        //checks that the entity is making a valid move
        if (direction == UP) {
            Object value = GameManager.getTable().getValueAt(yPos - 1, xPos);
            if (value == "--") {
                return false;
            }
            return true;
        }
        if (direction == DOWN) {
            Object value = GameManager.getTable().getValueAt(yPos + 1, xPos);
            if (value == "--") {
                return false;
            }
            return true;
        }
        if (direction == RIGHT) {
            Object value = GameManager.getTable().getValueAt(yPos, xPos + 1);
            if (value == "|") {
                return false;
            }
            return true;
        }
        if (direction == LEFT) {
            Object value = GameManager.getTable().getValueAt(yPos, xPos - 1);
            if (value == "|") {
                return false;
            }
            return true;
        }
        else {
            return false;
        }
    }
}
