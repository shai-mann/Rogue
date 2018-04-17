package map.level;

import entity.Entity;
import entity.Player;
import main.GameManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Passageway {
    public Passageway(Room roomTo, Room roomFrom) {
        createPassageway(roomFrom, roomTo);
    }
    private void drawHallwayMark(Point p) {
        GameManager.getTable().setValueAt("#", p.y, p.x);
    }
    private void createPassageway(Room roomFrom, Room roomTo) {
        Point centerRoomFrom = new Point(
                roomFrom.getTopLeft().x + (int) (roomFrom.getSize().getWidth() / 2),
                roomFrom.getTopLeft().y + (int) (roomFrom.getSize().getHeight() / 2)
        );
        Point centerRoomTo = new Point(
                roomTo.getTopLeft().x + (int) (roomTo.getSize().getWidth() / 2),
                roomTo.getTopLeft().y + (int) (roomTo.getSize().getHeight() / 2)
        );
        Point doorFrom = createDoor(centerRoomFrom, centerRoomTo);
        Point doorTo = createDoor(centerRoomTo, centerRoomFrom);

        equalize(doorFrom, doorTo);
    }
    private void equalize(Point start, Point end) {
        Point midwayPoint;
        if (start.getX() > end.getX()) {
            midwayPoint = new Point(end.x + new Random().nextInt(start.x - end.x), end.y);
        } else {
            midwayPoint = new Point(end.x - new Random().nextInt(end.x - start.x), end.y);
        }
        equalizeX(start, midwayPoint);
        equalizeY(start, end);
        equalizeX(midwayPoint, end);
        drawHallwayMark(end);
    }
    private void equalizeX(Point start, Point end) {
        while (start.getX() != end.getX()) {
            drawHallwayMark(start);
            if (start.getX() > end.getX()) {
                start.x -= 1;
            } else {
                start.x += 1;
            }
        }
    }
    private void equalizeY(Point start, Point end) {
        while (start.getY() != end.getY()) {
            drawHallwayMark(start);
            if (start.getY() > end.getY()) {
                start.y -= 1;
            } else {
                start.y += 1;
            }
        }
    }
    private Point createDoor(Point centerFrom, Point centerTo) {
        Point markerPoint = centerFrom;
        while (!isWall(markerPoint)) {
            markerPoint = getClosestPoint(getConnectedPoints(markerPoint), centerTo);
        }
        GameManager.getTable().setValueAt("+", markerPoint.y, markerPoint.x);
        return getFrontStep(markerPoint);
    }
    private boolean isWall(Point marker) {
        return GameManager.getTable().getValueAt(marker.y, marker.x).equals("=") ||
                GameManager.getTable().getValueAt(marker.y, marker.x).equals("|");
    }
    private Point getFrontStep(Point door) {
        Point temp;
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    temp = new Point(door.x, door.y - 1);
                    break;
                case 1:
                    temp = new Point(door.x, door.y + 1);
                    break;
                case 2:
                    temp = new Point(door.x + 1, door.y);
                    break;
                case 3:
                    temp = new Point(door.x - 1, door.y);
                    break;
                default:
                    temp = door;
                    break;
            }
            if (GameManager.getTable().getValueAt(temp.y, temp.x).equals("")) {
                return temp;
            }
        }
        return null;
    }
    private Point getClosestPoint(ArrayList<Point> points, Point end) {
        int shortestDistance = Integer.MAX_VALUE;
        int indexOfClosestPoint = -1;
        for (int i = 0; i < points.size(); i++) {
            if (getDistance(points.get(i), end) < shortestDistance) {
                shortestDistance = (int) getDistance(points.get(i), end);
                indexOfClosestPoint = i;
            }
        }
        return points.get(indexOfClosestPoint);
    }
    private double getDistance(Point p, Point p2) {
        return Math.sqrt(Math.pow(p2.getX() - p.getX(), 2) + Math.pow(p2.getY() - p.getY(), 2));
    }
    private ArrayList<Point> getConnectedPoints(Point p) {
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(p.x, p.y + 1));
        points.add(new Point(p.x, p.y - 1));
        points.add(new Point(p.x + 1, p.y));
        points.add(new Point(p.x - 1, p.y));

        return points;
    }
    private ArrayList<Integer> getDirections(Room room) {
        ArrayList<Integer> directions = new ArrayList<>();
        int zone = room.getZone();
        if (zone == 0) {
            // Doors can be down or right
            directions.add(Player.DOWN);
            directions.add(Player.RIGHT);
        } else if (zone == 1) {
            // Doors can be down right or left
            directions.add(Player.DOWN);
            directions.add(Player.RIGHT);
            directions.add(Player.LEFT);
        } else if (zone == 2) {
            // Doors can be down or left
            directions.add(Player.DOWN);
            directions.add(Player.LEFT);
        } else if (zone == 3) {
            // Doors can be up or right
            directions.add(Player.UP);
            directions.add(Player.RIGHT);
        } else if (zone == 4) {
            // Doors can be up right or left
            directions.add(Player.UP);
            directions.add(Player.RIGHT);
            directions.add(Player.LEFT);
        } else if (zone == 5) {
            // Doors can be up or left
            directions.add(Player.UP);
            directions.add(Player.LEFT);
        } else {
            throw new ArrayIndexOutOfBoundsException("Zone passed is not a valid zone.");
        }
        return directions;
    }
}
