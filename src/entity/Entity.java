package entity;

import helper.Helper;
import main.GameManager;

import java.awt.*;

public class Entity {
    private int xPos;
    private int yPos;

    public int health = 20;

    public String graphic;

    private String overWrittenGraphic = "";

    public static int UP = 0;
    public static int DOWN = 1;
    public static int RIGHT = 2;
    public static int LEFT = 3;

    public Entity(String g, int x, int y) {
        xPos = x;
        yPos = y;
        graphic = g;
        overWrittenGraphic = (String) GameManager.getTable().getValueAt(xPos, yPos);

        GameManager.add(graphic, x, y);
    }
    public boolean move(int direction) {
        if (checkValidMove(direction)) {
            GameManager.add(overWrittenGraphic, xPos, yPos);
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

            GameManager.add(graphic, xPos, yPos);
            return true;
        }
        return false;
    }
    public boolean checkValidMove(int direction) {
        //checks that the entity is making a valid move
        String value = (graphicAtMove(direction));
        return value.equals("-") || value.equals("+") || value.equals("#") || value.equals("*") || value.equals("]");
    }
    protected String graphicAtMove(int direction) {
        Object value;
        if (direction == UP) {
            value = GameManager.getTable().getValueAt(yPos - 1, xPos);
        }
        else if (direction == DOWN) {
            value = GameManager.getTable().getValueAt(yPos + 1, xPos);
        }
        else if (direction == RIGHT) {
            value = GameManager.getTable().getValueAt(yPos, xPos + 1);
        }
        else if (direction == LEFT) {
            value = GameManager.getTable().getValueAt(yPos, xPos - 1);
        } else {
            value = "";
        }
        return (String) value;
    }
    protected boolean isNextTo(Entity entity) {
        return
                ((entity.getXPos() + 1 == getXPos() || entity.getXPos() - 1 == getXPos()) && entity.getYPos() == getYPos()) ||
                        ((entity.getYPos() + 1 == getYPos() || entity.getYPos() - 1 == getYPos()) && entity.getXPos() == getXPos());
    }

    public int getXPos() {
        return xPos;
    }
    public int getYPos() {
        return yPos;
    }
    public void setLocation(Point p) {
        GameManager.getTable().setValueAt(overWrittenGraphic, xPos, yPos);

        xPos = p.x;
        yPos = p.y;
        overWrittenGraphic = (String) GameManager.getTable().getValueAt(yPos, xPos);

        GameManager.getTable().setValueAt("@", p.y, p.x);
    }
}
