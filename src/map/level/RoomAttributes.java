package map.level;

import util.Helper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RoomAttributes {

    private final Dimension size;
    private final Point topLeft;

    private final Rectangle bounds;
    private final List<Point> points = new ArrayList<>();
    private final List<Point> wallPoints = new ArrayList<>();

    public RoomAttributes(Point topLeft, Dimension size) {
        this.size = size;
        this.topLeft = topLeft;

        // bounds are one longer because rectangles don't consider boundary included when running contains()
        bounds = new Rectangle(topLeft(), Helper.translate(size, new Dimension(1, 1)));

        initPoints();
    }

    private void initPoints() {
        for (int i = x(); i <= x() + width(); i++) {
            for (int j = y(); j <= y() + height(); j++) {
                points.add(new Point(i, j));
            }
        }

        wallPoints.addAll(getPoints().stream().filter(
                (p) -> p.x == topLeft().x ^ p.y == topLeft().y ^
                        p.x == topLeft().x + size().width ^ p.y == topLeft().y + size().height
        ).toList());
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

    public List<Point> getWallPoints() {
        return wallPoints;
    }

    public List<Point> getNonWallPoints() {
        return points.stream().filter(p -> !wallPoints.contains(p)).toList();
    }
}
