package map.level;

import main.GameManager;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Room {

    public static ArrayList<Polygon> zones = new ArrayList<>();
    public static ArrayList<Room> rooms = new ArrayList<>();
    public ArrayList<Point> doors = new ArrayList<>();
    // TODO: Fix that rooms can generate inside of each other

    private Polygon polygon;

    private Dimension size;
    private Point topLeft;

    public Room(Point p, Dimension size) {
        rooms.add(this);
        createBounds(p.x, p.y, size);

        this.size = size;
        topLeft = p;

        addRoom();
    }
    public int getZone() {
        for (int i = 0; i < zones.size(); i++) {
            if (zones.get(i).intersects(getBounds().getBounds())) {
                return i;
            }
        }
        return -1;
    }
    public Polygon getBounds() {
        return polygon;
    }
    public Point getTopLeft() {
        return topLeft;
    }
    public Dimension getSize() {
        return size;
    }
    private void addRoom() {
        String[] rowData;
        for (int i = 0; i < size.getHeight(); i++) {
            if (i == 0 || i == size.getHeight() - 1) {
                rowData = createDefaultRow("=", "=");
            } else {
                rowData = createDefaultRow("|", "-");
            }
            for (int z = 0; z < rowData.length; z++) {
                GameManager.getTable().getCustomModel().setValueAt(rowData[z], topLeft.y + i, topLeft.x + z);
            }
        }
    }
    public static boolean checkValidSpace(int x, int y, Dimension size) {
        int[] xPoints = {x, x + size.width - 1, x + size.width - 1, x};
        int[] yPoints = {y, y, y + size.height - 1, y + size.height - 1};

        Polygon tempBounds = new Polygon(xPoints, yPoints, 4);
        for (Room room : rooms ) {
            int[] xPoints1 = {room.topLeft.x + 5, room.topLeft.x + room.getSize().width + 5,
                    room.topLeft.x + room.getSize().width + 5, room.topLeft.x + 5};
            int[] yPoints1 = {room.topLeft.y + 5, room.topLeft.y + 5,
                    room.topLeft.y + room.getSize().height + 5, room.topLeft.y + room.getSize().height + 5};
            Polygon temp = new Polygon(xPoints1, yPoints1, 4);
            if (temp.intersects(tempBounds.getBounds())) {
                return false;
            }
        }
        if (tempBounds.getBounds().getMaxX() > Level.getLevel().getTable().getColumnCount() - 2 ||
                tempBounds.getBounds().getMinX() < 0 ||
                tempBounds.getBounds().getMaxY() > Level.getLevel().getTable().getRowCount() - 2 ||
                tempBounds.getBounds().getMinY() < 0) {
            return false;
        }
        return true;
    }
    private String[] createDefaultRow(String edges, String rests) {
        String[] rowValueList = new String[(int) size.getWidth()];

        Arrays.fill(rowValueList, rests);
        rowValueList[0] = edges;
        rowValueList[size.width - 1] = edges;

        return rowValueList;
    }
    private void createBounds(int x, int y, Dimension size) {
        int[] xPoints = {x, x + size.width - 1, x + size.width - 1, x};
        int[] yPoints = {y, y, y + size.height - 1, y + size.height - 1};

        polygon = new Polygon(xPoints, yPoints, 4);
    }
    public static ArrayList<Polygon> setZones() {
        ArrayList<Polygon> zones = new ArrayList<>();
        int[] xPoints = {0, 23, 23, 0};
        int[] yPoints = {0, 0, 20, 20};
        zones.add(0, new Polygon(xPoints, yPoints, 4));
        int[] xPoints1 = {24, 46, 46, 24};
        int[] yPoints1 = {0, 0, 40, 40};
        zones.add(1, new Polygon(xPoints1, yPoints1, 4));
        int[] xPoints2 = {25, 46, 46, 25};
        int[] yPoints2 = {0, 0, 20, 20};
        zones.add(2, new Polygon(xPoints2, yPoints2, 4));
        int[] xPoints3 = {25, 46, 46, 25};
        int[] yPoints3 = {20, 20, 40, 40};
        zones.add(3, new Polygon(xPoints3, yPoints3, 4));
        int[] xPoints4 = {47, 69, 69, 47};
        int[] yPoints4 = {0, 0, 20, 20};
        zones.add(4, new Polygon(xPoints4, yPoints4, 4));
        int[] xPoints5 = {47, 69, 69, 47};
        int[] yPoints5 = {20, 20, 40, 40};
        zones.add(5, new Polygon(xPoints5, yPoints5, 4));
        return zones;
    }
}
