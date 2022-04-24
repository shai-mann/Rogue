package map.level;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RoomAttributes {

    private final Dimension size;
    private final Point topLeft;
    private final List<Door> doors = new ArrayList<>();

    private final Rectangle bounds;
    private final List<Point> points = new ArrayList<>();

    public RoomAttributes(Point topLeft, Dimension size) {
        this.size = size;
        this.topLeft = topLeft;

        bounds = new Rectangle(topLeft(), size());

        initPoints();
    }

    private void initPoints() {
        for (int i = x(); i < x() + width(); i++) {
            for (int j = y(); j < y() + height(); j++) {
                points.add(new Point(i, j));
            }
        }
    }

    /* SETTERS */

    public void addDoors(Door... doors) {
        this.doors.addAll(List.of(doors));
    }

    /* GETTERS */

    public Point topLeft() {
        return topLeft;
    }

    public Dimension size() {
        return size;
    }

    public Rectangle bounds() {
        return bounds;
    }

    public List<Point> getPoints() {
        return points;
    }

    public int x() {
        return topLeft.x;
    }

    public int y() {
        return topLeft.y;
    }

    public int width() {
        return size.width;
    }

    public int height() {
        return size.height;
    }
}
