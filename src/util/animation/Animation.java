package util.animation;


import org.jetbrains.annotations.Nullable;

import entity.livingentity.Player;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Animation implements Serializable {

    private ArrayList<Point> points = new ArrayList<>();
    private ArrayList<Point> queuedPoints = new ArrayList<>();
    private Color color;
    private Point center;
    private int direction = -1;
    private int iterations = 0;

    // Constructors

    public Animation(Point center, Color c) {
        // Explosion
        points.add(center);
        this.center = center;
        color = c;
    }
    public Animation(Point center, Color c, int direction) {
        // Cone
        points.add(center);
        this.center = center;
        this.direction = direction;
        color = c;
    }

    // update

    public void update() {
        iterations++;
        if (direction == -1) {
            // Explosion
            for (Point p : points) {
                addPoint(new Point(p.x,p.y + 1));
                addPoint(new Point(p.x,p.y - 1));
                addPoint(new Point(p.x + 1, p.y));
                addPoint(new Point(p.x - 1, p.y));
            }
        } else {
            // Cone
            if (direction == Player.DOWN) {
                for (int i = points.size() - 1; i >= points.size() - (iterations * 2); i--) {
                    Point p = points.get(i);
                    addPoint(new Point(p.x, p.y + 1));
                    addPoint(new Point(p.x + 1, p.y + 1));
                    addPoint(new Point(p.x - 1, p.y + 1));
                }
            } else if (direction == Player.UP) {
                for (int i = points.size() - 1; i >= points.size() - (iterations * 2); i--) {
                    Point p = points.get(i);
                    addPoint(new Point(p.x, p.y - 1));
                    addPoint(new Point(p.x + 1, p.y - 1));
                    addPoint(new Point(p.x - 1, p.y - 1));
                }
            } else if (direction == Player.LEFT) {
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
        for (Point point : points) {
            if (p.getX() == point.getX() && p.getY() == point.getY()) {
                return;
            }
        }
        queuedPoints.add(p);
    }
    public int getIterations() {
        return iterations;
    }
    public boolean contains(Point p) {
        for (Point point : points) {
            if (p.getX() == point.getX() && p.getY() == point.getY()) {
                return true;
            }
        }
        return false;
    }
    public ArrayList<Point> getPoints() {
        return points;
    }
    public Color getColor() {
        return color;
    }
}
