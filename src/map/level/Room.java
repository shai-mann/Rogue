package map.level;

import map.level.table.CustomRoomTable;
import rendering.Renderable;
import rendering.RoomRenderer;
import util.Helper;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable, Renderable {

    public static ArrayList<Room> rooms = new ArrayList<>(); // TODO: remove
    public ArrayList<Door> doors = new ArrayList<>();
    public ArrayList<Passageway> passageways = new ArrayList<>();

    private final RoomRenderer renderer;
    private final RoomAttributes attributes;

    public Room(Point topLeft, Dimension size) {
        rooms.add(this);

        attributes = new RoomAttributes(topLeft, size);
        renderer = new RoomRenderer(attributes);
    }

    @Override
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

    // ROOM GENERATION METHODS
    public static boolean checkValidSpace(int x, int y, Dimension size) {
        int[] xPoints = {x - 2, x + size.width + 2, x + size.width + 2, x - 2};
        int[] yPoints = {y - 2, y - 2, y + size.height + 2, y + size.height + 2};

        Polygon tempBounds = new Polygon(xPoints, yPoints, 4);
        for (Room room : rooms ) {
            RoomAttributes attr = room.attributes;
            
            int[] xPoints1 = { attr.x(), attr.x() + attr.width(), attr.x() + attr.width(), attr.x() };
            int[] yPoints1 = { attr.y(), attr.y(), attr.y() + attr.height(), attr.y() + attr.height() };

            Polygon temp = new Polygon(xPoints1, yPoints1, 4);

            if (temp.intersects(tempBounds.getBounds())) {
                return false;
            }
        }
        if (tempBounds.getBounds().getMaxX() > Level.getLevel().getHiddenTable().getColumnCount() - 2 ||
                tempBounds.getBounds().getMinX() < 0 ||
                tempBounds.getBounds().getMaxY() > Level.getLevel().getHiddenTable().getRowCount() - 2 ||
                tempBounds.getBounds().getMinY() < 0) {
            return false;
        }
        return true;
    }

    // GETTER METHODS

    public Point getTopLeft() {
        return attributes.topLeft();
    }
    public Dimension getSize() {
        return attributes.size();
    }
    public Rectangle bounds() {
        return attributes.bounds();
    }
    public Point getRandomPointInBounds() {
        return Helper.getRandom(attributes.getPoints());
        // todo: prevent from overlapping with other entities when spawning

//        Point p;
//        do {
//            p = Helper.getRandom(attributes.getPoints());
//        } while (!GameManager.getTable().getValueAt(p.y, p.x).equals("-"));
//
//        return p;
    }
}
