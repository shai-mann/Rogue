package map.level;

import entity.Player;
import main.GameManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Passageway {
    /*
    * Passageway class creates the passages between the rooms, and also the doors in the rooms.
    * It will need to add the door coordinates to the rooms as it creates them.
     */

    private int previousDirection = -1;

    public Passageway(Room roomFrom, Room roomTo) {

        Point doorFrom = createDoorway(roomFrom);
        Point doorTo = createDoorway(roomTo);

        createPassageway(doorFrom, doorTo);
    }
    private void createPassageway(Point start, Point end) {
        Point markerPoint = start;

        while (!checkForDoorway(markerPoint, end)) {
            ArrayList<Point> surroundingPoints = getConnectedPoints(markerPoint);
            Point movementPoint = getClosestPoint(surroundingPoints, end);
            previousDirection = updatePreviousDirection(markerPoint, movementPoint);
            markerPoint = drawHallwayMark(movementPoint);
        }
    }
    private boolean checkForDoorway(Point marker, Point end) {
        if (marker.getX() + 1 == end.getX() && marker.getY() == end.getY()) {
            return true;
        } else if (marker.getX() - 1 == end.getX() && marker.getY() == end.getY()) {
            return true;
        } else if (marker.getY() + 1 == end.getY() && marker.getX() == end.getX()) {
            return true;
        } else if (marker.getY() - 1 == end.getY() && marker.getX() == end.getX()) {
            return true;
        } else {
            return false;
        }
    }
    private Point drawHallwayMark(Point p) {
        GameManager.add("#", p.x, p.y);
        return p;
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
    private int updatePreviousDirection(Point marker, Point end) {
        if (marker.getY() > end.getY()) {
            return Player.UP;
        } else if (marker.getY() < end.getY()) {
            return Player.DOWN;
        } else if (marker.getX() > end.getX()) {
            return Player.RIGHT;
        } else {
            return Player.LEFT;
        }
    }
    private ArrayList<Point> removeInvalidPoints(ArrayList<Point> points) {
        if (previousDirection != -1) {
            points.remove(previousDirection);
        }
        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            if (!GameManager.getTable().getValueAt(p.y, p.x).equals("")) {
                points.remove(p);
            }
        }
        return points;
    }
    private ArrayList<Point> getConnectedPoints(Point p) {
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(p.x, p.y + 1));
        points.add(new Point(p.x, p.y - 1));
        points.add(new Point(p.x + 1, p.y));
        points.add(new Point(p.x - 1, p.y));

        return removeInvalidPoints(points);
    }
    private Point createDoorway(Room room) {
        ArrayList<Integer> directions = getDirections(room.getZone());
        Random random = new Random();
        Point door = new Point();

        int maxXPos = (int) room.getCenter().getX() + (int) Math.floor(room.getSize().getWidth() / 2);
        int maxYPos = (int) room.getCenter().getY() + (int) Math.floor(room.getSize().getHeight() / 2);
        int minXPos = (int) room.getCenter().getX() - (int) Math.floor(room.getSize().getWidth() / 2);
        int minYPos = (int) room.getCenter().getY() - (int) Math.floor(room.getSize().getWidth() / 2);

        int doorFrom = directions.get(new Random().nextInt(directions.size()));

        if (doorFrom == Player.DOWN) {
            door =
                    new Point(random.nextInt(maxXPos - minXPos - 2) + minXPos + 1, (int) (room.getCenter().getY() + Math.floor(room.getSize().getHeight() / 2)));
            GameManager.getTable().getModel().setValueAt("+", door.y, door.x);
        } else if (doorFrom == Player.UP) {
            door =
                    new Point(random.nextInt(maxXPos - minXPos - 2) + minXPos + 1, (int) Math.floor(room.getCenter().getY() - Math.floor(room.getSize().getHeight() / 2)));
            GameManager.getTable().getModel().setValueAt("+", door.y, door.x);
        } else if (doorFrom == Player.RIGHT) {
            door =
                    new Point((int) Math.floor(room.getCenter().getX() + Math.floor(room.getSize().getWidth() / 2)), random.nextInt(maxYPos - minYPos - 2) + minYPos + 1);
            GameManager.getTable().getModel().setValueAt("+", door.y, door.x);
        } else if (doorFrom == Player.LEFT) {
            door =
                    new Point((int) Math.floor(room.getCenter().getX() - Math.floor(room.getSize().getWidth() / 2)), random.nextInt(maxYPos - minYPos - 2) + minYPos + 1);
            GameManager.getTable().getModel().setValueAt("+", door.y, door.x);
        }
        room.doors.add(door);
        return door;
    }
    private ArrayList<Integer> getDirections(int zone) {
        ArrayList<Integer> directions = new ArrayList<>();
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
            // Doors can be up down or right
            directions.add(Player.DOWN);
            directions.add(Player.RIGHT);
        } else if (zone == 4) {
            // Doors can be any direction
            directions.add(Player.DOWN);
            directions.add(Player.UP);
            directions.add(Player.RIGHT);
            directions.add(Player.LEFT);
        } else if (zone == 5) {
            // Doors can be up down or left
            directions.add(Player.DOWN);
            directions.add(Player.UP);
            directions.add(Player.LEFT);
        } else if (zone == 6) {
            // Doors can be up or right
            directions.add(Player.UP);
            directions.add(Player.RIGHT);
        } else if (zone == 7) {
            // Doors can be up right or left
            directions.add(Player.UP);
            directions.add(Player.RIGHT);
            directions.add(Player.LEFT);
        } else if (zone == 8) {
            // Doors can be up or left
            directions.add(Player.UP);
            directions.add(Player.LEFT);
        } else {
            throw new ArrayIndexOutOfBoundsException("Zone passed is not a valid zone.");
        }
        return directions;
    }
    private double getDistance(Point p, Point p2) {
        return Math.sqrt(Math.pow(p2.getX() - p.getX(), 2) + Math.pow(p2.getY() - p.getY(), 2));
    }
}
