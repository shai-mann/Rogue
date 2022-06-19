package util.animation;


import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Animation implements Serializable {

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        ALL
    }

    private final List<Point> points = new ArrayList<>();
    private final Set<Point> queuedPoints = new HashSet<>();
    private final Color color;
    private final Point center;
    private Direction direction = Direction.ALL;
    private int iterations = 0;

    // Constructors

    public Animation(Point center, Color c) {
        // Explosion
        points.add(center);
        this.center = center;
        color = c;
    }
    public Animation(Point center, Color c, Direction direction) {
        // Cone
        points.add(center);
        this.center = center;
        this.direction = direction;
        color = c;
    }

    // update

    public void update() {
        iterations++;
        if (direction == Direction.ALL) {
            // Explosion
            for (Point p : points) {
                addPoint(new Point(p.x,p.y + 1));
                addPoint(new Point(p.x,p.y - 1));
                addPoint(new Point(p.x + 1, p.y));
                addPoint(new Point(p.x - 1, p.y));
            }
        } else {
            // Cone
            if (direction == Direction.DOWN) {
                for (int i = points.size() - 1; i >= points.size() - (iterations * 2); i--) {
                    Point p = points.get(i);
                    addPoint(new Point(p.x, p.y + 1));
                    addPoint(new Point(p.x + 1, p.y + 1));
                    addPoint(new Point(p.x - 1, p.y + 1));
                }
            } else if (direction == Direction.UP) {
                for (int i = points.size() - 1; i >= points.size() - (iterations * 2); i--) {
                    Point p = points.get(i);
                    addPoint(new Point(p.x, p.y - 1));
                    addPoint(new Point(p.x + 1, p.y - 1));
                    addPoint(new Point(p.x - 1, p.y - 1));
                }
            } else if (direction == Direction.LEFT) {
                for (int i = points.size() - 1; i >= points.size() - (iterations * 2); i--) {
                    Point p = points.get(i);
                    addPoint(new Point(p.x - 1, p.y));
                    addPoint(new Point(p.x - 1, p.y - 1));
                    addPoint(new Point(p.x - 1, p.y + 1));
                }
            } else {
                // direction == Player.RIGHT
                for (int i = points.size() - 1; i >= points.size() - (iterations * 2); i--) {
                    Point p = points.get(i);
                    addPoint(new Point(p.x + 1, p.y));
                    addPoint(new Point(p.x + 1, p.y - 1));
                    addPoint(new Point(p.x + 1, p.y + 1));
                }
            }
        }
        points.addAll(queuedPoints);
        queuedPoints.clear();
    }
    private void addPoint(Point p) {
        queuedPoints.add(p);
    }
    public int getIterations() {
        return iterations;
    }
    public boolean contains(Point p) {
        return points.contains(p);
    }
    public List<Point> getPoints() {
        return points;
    }
    public Color getColor() {
        return color;
    }
}
