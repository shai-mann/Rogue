package entity;

import entity.monster.Monster;
import helper.Helper;
import main.GameManager;

import java.awt.*;

public class Entity {
    private int xPos;
    private int yPos;

    public int health = 20;

    public String graphic;

    public String overWrittenGraphic = "-";

    public static int UP = 0;
    public static int DOWN = 1;
    public static int RIGHT = 2;
    public static int LEFT = 3;

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
//        for (int i = 0; i < Monster.getMonsters().size(); i++) {
//            Monster monster = Monster.getMonsters().get(i);
//            if (fakeMove(direction).getY() == monster.getYPos() && fakeMove(direction).getX() == monster.getXPos()) {
//                return false;
//            }
//        }
        // TODO: implement so if monster is invisible, then it will not be allowed to walk there
        return value.equals("-") || value.equals("+") || value.equals("#") || value.equals("*") || value.equals("]") || value.equals("&");
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
    private Point fakeMove(int direction) {
        Point point;
        if (direction == UP) {
            point = new Point(yPos - 1, xPos);
        }
        else if (direction == DOWN) {
            point = new Point(yPos + 1, xPos);
        }
        else if (direction == RIGHT) {
            point = new Point(yPos, xPos + 1);
        }
        else if (direction == LEFT) {
            point = new Point(yPos, xPos - 1);
        } else {
            point = new Point();
        }
        return point;
    }
    protected boolean isNextTo(Entity entity) {
        return
                ((entity.getXPos() + 1 == getXPos() || entity.getXPos() - 1 == getXPos()) && entity.getYPos() == getYPos()) ||
                        ((entity.getYPos() + 1 == getYPos() || entity.getYPos() - 1 == getYPos()) && entity.getXPos() == getXPos());
    }

    // GETTER/SETTER METHODS

    public double getDistanceTo(Entity entity) {
        return Math.hypot(getXPos() - entity.getXPos(), getYPos() - entity.getYPos());
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
