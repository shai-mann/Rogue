package map.level;

import map.level.table.CustomRoomTable;
import rendering.PassagewayRenderer;
import rendering.Renderable;
import util.Helper;
import main.GameManager;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Passageway implements Serializable, Renderable {

    // CONSTRUCTORS

    private final PassagewayRenderer renderer;
    private final List<Point> points = new ArrayList<>();

    public Passageway(Room roomTo, Room roomFrom) {
        createPassageway(roomFrom, roomTo);

        renderer = new PassagewayRenderer(points);
    }

    public void addShownPoints(List<Point> points) {
        renderer.addShownPoints(points);
    }

    @Override
    public List<Point> getShownPoints() {
        return renderer.getShownPoints();
    }

    @Override
    public void reveal() {
        renderer.reveal();
    }

    @Override
    public void render(CustomRoomTable table) {
        renderer.render(table);
    }

    public boolean contains(Point p) {
        return points.contains(p); // TODO: test this works?
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
        points.add(p);
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
        do {
            if (Math.abs(start.getX() - end.getX()) > Math.abs(start.getY() - end.getY())) {
                if (start.getX() > end.getX()) {
                    markerPoint = new Point(end.x + Helper.random.nextInt(start.x - end.x), end.y);
                } else if (start.getX() < end.getX()) {
                    markerPoint = new Point(end.x - Helper.random.nextInt(end.x - start.x), end.y);
                } else {
                    markerPoint = getFrontStep(end);
                }
            } else {
                if (start.getY() > end.getY()) {
                    markerPoint = new Point(end.x, end.y + Helper.random.nextInt(start.y - end.y));
                } else if (start.getY() < end.getY()) {
                    markerPoint = new Point(end.x, end.y - Helper.random.nextInt(end.y - start.y));
                } else {
                    markerPoint = getFrontStep(end);
                }
            }
            if (!GameManager.getTable().getValueAt(markerPoint.y, markerPoint.x).equals("")) {
                markerPoint = null;
            }
        } while (markerPoint == null);
        return markerPoint;
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
        room.doors.add(new Door(markerPoint, isShown(), (String) GameManager.getTable().getValueAt(markerPoint.y, markerPoint.x)));
        if (!room.doors.get(room.doors.size() - 1).isSecret()) {
            GameManager.getTable().setValueAt("+", markerPoint.y, markerPoint.x);
        }
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
            temp = switch (i) {
                case 0 -> new Point(door.x, door.y - 1);
                case 1 -> new Point(door.x, door.y + 1);
                case 2 -> new Point(door.x + 1, door.y);
                case 3 -> new Point(door.x - 1, door.y);
                default -> door;
            };
            if (GameManager.getTable().getValueAt(temp.y, temp.x).equals("")) {
                return temp;
            }
        }
        return door;
    }
    private Point getClosestPoint(ArrayList<Point> points, Point end) {
        double shortestDistance = Integer.MAX_VALUE;
        int indexOfClosestPoint = -1;
        for (int i = 0; i < points.size(); i++) {
            if (getDistance(points.get(i), end) < shortestDistance) {
                shortestDistance = getDistance(points.get(i), end);
                indexOfClosestPoint = i;
            }
        }
        return points.get(indexOfClosestPoint);
    }

    // TODO: move to Helper?
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
    private boolean isShown() {
        int percentChance = Level.getLevel().getLevelNumber() > 11 ? 50 : 3 * Level.getLevel().getLevelNumber();

        return !Helper.calculateChance(percentChance / 100.0);
    }
}
