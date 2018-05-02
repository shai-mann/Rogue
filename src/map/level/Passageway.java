package map.level;

import helper.Helper;
import main.GameManager;

import java.awt.*;
import java.util.ArrayList;

public class Passageway {

    // CONSTRUCTORS

    public Passageway(Room roomTo, Room roomFrom) {
        createPassageway(roomFrom, roomTo);
    }
    public Passageway(Room room) {
        // this passageway will just wander off randomly for a ways. It will be based on where the
    }

    // PASSAGEWAY GENERATION METHODS

    private void createPassageway(Room roomFrom, Room roomTo) {
        Point centerRoomFrom = new Point(
                roomFrom.getTopLeft().x + (int) (roomFrom.getSize().getWidth() / 2),
                roomFrom.getTopLeft().y + (int) (roomFrom.getSize().getHeight() / 2)
        );
        Point centerRoomTo = new Point(
                roomTo.getTopLeft().x + (int) (roomTo.getSize().getWidth() / 2),
                roomTo.getTopLeft().y + (int) (roomTo.getSize().getHeight() / 2)
        );
        Point doorFrom = createDoor(roomFrom, centerRoomFrom, centerRoomTo);
        Point doorTo = createDoor(roomTo, centerRoomTo, centerRoomFrom);

        equalize(doorFrom, doorTo);
    }
    private void drawHallwayMark(Point p) {
        if (GameManager.getTable().getValueAt(p.y, p.x).equals("") || GameManager.getTable().getValueAt(p.y, p.x).equals("#")) {
            GameManager.getTable().setValueAt("#", p.y, p.x);
        }
    }

    // EQUALIZE METHODS

    private void equalize(Point start, Point end) {
        Point markerPoint = getMarkerPoint(start, end);
        if (start.getX() - end.getX() > start.getY() - end.getY()) {
            equalizeX(start, markerPoint);
            equalizeY(start, end);
            equalizeX(markerPoint, end);
            drawHallwayMark(end);
        } else {
            equalizeY(start, markerPoint);
            equalizeX(start, end);
            equalizeY(markerPoint, end);
            drawHallwayMark(end);
        }
    }

    // EQUALIZE HELPER METHODS

    private Point getMarkerPoint(Point start, Point end) {
        Point markerPoint;
        if (start.getX() - end.getX() > start.getY() - end.getY()) {
            if (start.getX() > end.getX()) {
                markerPoint = new Point(end.x + Helper.random.nextInt(start.x - end.x), end.y);
            } else if (start.getX() < end.getX()){
                markerPoint = new Point(end.x - Helper.random.nextInt(end.x - start.x), end.y);
            } else {
                markerPoint = new Point(end.x, end.y);
            }
        } else {
            if (start.getY() > end.getY()) {
                markerPoint = new Point(end.x, end.y + Helper.random.nextInt(start.y - end.y));
            } else if (start.getY() < end.getY()){
                markerPoint = new Point(end.x, end.y - Helper.random.nextInt(end.y - start.y));
            } else {
                markerPoint = new Point(end.x, end.y);
            }
        }
        if (GameManager.getTable().getValueAt(markerPoint.y, markerPoint.x).equals("")) {
            return markerPoint;
        } else {
            return getMarkerPoint(start, end);
        }
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

    // DOORWAY HELPER METHODS

    private Point createDoor(Room room, Point centerFrom, Point centerTo) {
        Point markerPoint = centerFrom;
        while (!isWall(markerPoint)) {
            markerPoint = getClosestPoint(getConnectedPoints(markerPoint), centerTo);
        }
        GameManager.getTable().setValueAt("+", markerPoint.y, markerPoint.x);
        room.doors.add(new Door(markerPoint, getIsSecret(), (String) GameManager.getTable().getValueAt(markerPoint.y, markerPoint.x)));
        room.passageways.add(this);
        return getFrontStep(markerPoint);
    }
    private boolean isWall(Point marker) {
        return GameManager.getTable().getValueAt(marker.y, marker.x).equals("=") ||
                GameManager.getTable().getValueAt(marker.y, marker.x).equals("|") ||
                GameManager.getTable().getValueAt(marker.y, marker.x).equals("+");
    }

    // OTHER HELPER METHODS

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
        return door;
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
    public static double getDistance(Point p, Point p2) {
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
    private boolean getIsSecret() {
        int randomInt = Helper.random.nextInt(100) + 1;
        // TODO: Make this only have a maximum of 50% chance to be secret
        return randomInt >= 100 - (5* Level.getLevel().getLevelNumber());
    }
}
