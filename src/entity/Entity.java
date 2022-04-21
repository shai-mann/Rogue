package entity;

import entity.livingentity.monster.Monster;
import main.GameManager;
import util.Helper;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

public class Entity implements Serializable {
    private int xPos;
    private int yPos;

    public int health = 20;

    public String graphic;
    protected String name = "<Default>";
    public String overWrittenGraphic;

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int RIGHT = 2;
    public static final int LEFT = 3;
    public static final List<Integer> DIRECTIONS = List.of(new Integer[]{ UP, DOWN, LEFT, RIGHT });

    public Entity(String g, int x, int y) {
        xPos = x;
        yPos = y;
        graphic = g;
        overWrittenGraphic = (String) GameManager.getTable().getValueAt(yPos, xPos);

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

    // MOVEMENT HELPER METHODS

    private boolean checkValidMove(int direction) {
        //checks that the entity is making a valid move
        String value = (graphicAtMove(direction));
        for (int i = 0; i < Monster.getMonsters().size(); i++) {
            Monster monster = Monster.getMonsters().get(i);
            if (fakeMove(direction).getY() == monster.getYPos() && fakeMove(direction).getX() == monster.getXPos()) {
                return false;
            }
        }
        return value.equals("-") || value.equals("+") || value.equals("#") ||
                value.equals("*") || value.equals("]") || value.equals("&") || value.equals("%") ||
                value.equals("?") || value.equals("/") || value.equals("^") || value.equals(":") ||
                value.equals("!") || value.equals(")") || value.equals(",");
    }
    private String graphicAtMove(int direction) {
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
    protected Point fakeMove(int direction) {
        Point point;
        if (direction == UP) {
            point = new Point(xPos, yPos - 1);
        }
        else if (direction == DOWN) {
            point = new Point(xPos, yPos + 1);
        }
        else if (direction == RIGHT) {
            point = new Point(xPos + 1, yPos);
        }
        else if (direction == LEFT) {
            point = new Point(xPos - 1, yPos);
        } else {
            point = new Point();
        }
        return point;
    }

    // GETTER/SETTER METHODS

    public Point getPointNextTo() {
        int direction = Helper.random.nextInt(4) + 1;
        switch (direction) {
            case UP:
                return new Point(getXPos(), getYPos() - 1);
            case DOWN:
                return new Point(getXPos(), getYPos() + 1);
            case RIGHT:
                return new Point(getXPos() + 1, getYPos());
            case LEFT:
                return new Point(getXPos() - 1, getYPos());
            default:
                return new Point();
        }
    }
    public int getXPos() {
        return xPos;
    }
    public int getYPos() {
        return yPos;
    }
    public void setLocation(Point p) {
        GameManager.getTable().setValueAt(overWrittenGraphic, yPos, xPos);
        xPos = p.x;
        yPos = p.y;
        overWrittenGraphic = (String) GameManager.getTable().getValueAt(yPos, xPos);

        GameManager.getTable().setValueAt(this.graphic, p.y, p.x);
    }
    public Point getLocation() {
        return new Point(getXPos(), getYPos());
    }
    public String getName() {
        return name;
    }
}
