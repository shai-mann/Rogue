package entity.lifeless;

import entity.structure.AbstractEntity;
import entity.structure.Entity;
import entity.structure.EntityProperties;

import java.awt.*;

public class Staircase extends AbstractEntity implements Entity {

    private static final String STAIRCASE_GRAPHIC = "%", STAIRCASE_NAME = "Staircase";
    private static final EntityProperties STAIRCASE_PROPERTIES = new EntityProperties(
            STAIRCASE_NAME, STAIRCASE_GRAPHIC, Color.BLUE
    );
    public enum Direction {
        DOWN,
        UP
    }

    private Direction direction = Direction.DOWN;

    public Staircase(Point location, Direction direction) {
        super(STAIRCASE_PROPERTIES, location);
        this.direction = direction;
    }
    public Direction getDirection() {
        return direction;
    }
}
