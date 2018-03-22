package map.level;

import map.RoomTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Room {

    private static ArrayList<Polygon> zones = new ArrayList<>();
    private static ArrayList<Room> rooms = new ArrayList<>();
    private ArrayList<Point> doors = new ArrayList<>();

    private Polygon polygon;

    private Point center;
    private RoomTableModel model;
    private Dimension size;

    public Room(RoomTableModel model, int x, int y, Dimension size) {
        zones = setZones();
        createBounds(x, y, size);

        this.model = model;
        center = new Point(x, y);
        this.size = size;

        addRoom();
    }
    public int getZone() {
        for (int i = 0; i < zones.size(); i++) {
            if (zones.get(i).contains(center)) {
                return i;
            }
        }
        return zones.size();
    }
    public Polygon getBounds() {
        return polygon;
    }
    private void addRoom() {
        Point startingPoint = new Point((int) (center.getX() + Math.floor((size.getWidth() / 2))), (int) (center.getY() + Math.floor((size.getHeight() / 2))));
        String[] rowData;
        for (int i = 0; i < size.getHeight(); i++) {
            if (i == 0 || i == size.getHeight()) {
                rowData = createDefaultRow("=", "=");
            } else {
                rowData = createDefaultRow("|", "-");
            }
            for (int z = 0; z < rowData.length; z++) {
                model.setValueAt(rowData[z], startingPoint.y - i, startingPoint.x + (z - 1));
            }
        }
    }
    public static boolean checkValidSpace(int x, int y, Dimension size) {
        Rectangle tempBounds = new Rectangle(x, y, (int) size.getWidth(), (int) size.getHeight());
        for (Room room : rooms ) {
            if (room.getBounds().contains(tempBounds)) {
                return false;
            }
        }
        return true;
    }
    private String[] createDefaultRow(String edges, String rests) {
        String[] rowValueList = new String[(int) size.getWidth()];

        Arrays.fill(rowValueList, rests);
        rowValueList[0] = edges;
        rowValueList[(int) size.getWidth()] = edges;

        return rowValueList;
    }
    private void createBounds(int x, int y, Dimension size) {
        int[] xPoints = {(int) (x + (size.getWidth() / 2)), (int) (x - (size.getWidth() / 2))};
        int[] yPoints = {(int) (y + (size.getHeight()) / 2), (int) (y - (size.getHeight()) / 2)};

        polygon = new Polygon(xPoints, yPoints, 4);
    }
    private ArrayList<Polygon> setZones() {
        ArrayList<Polygon> zones = new ArrayList<>();
        int[] xPoints = {0, 33, 0, 33};
        int[] yPoints = {33, 0, 33, 0};
        zones.set(0, new Polygon(xPoints, yPoints, 4));
        int[] xPoints1 = {34, 66, 34, 66};
        int[] yPoints1 = {33, 0, 33, 0};
        zones.set(1, new Polygon(xPoints1, yPoints1, 4));
        int[] xPoints2 = {67, 99, 67, 99};
        int[] yPoints2 = {33, 0, 33, 0};
        zones.set(2, new Polygon(xPoints2, yPoints2, 4));
        int[] xPoints3 = {0, 33, 0, 33};
        int[] yPoints3 = {34, 66, 34, 66};
        zones.set(3, new Polygon(xPoints3, yPoints3, 4));
        int[] xPoints4 = {34, 66, 34, 66};
        int[] yPoints4 = {34, 66, 34, 66};
        zones.set(4, new Polygon(xPoints4, yPoints4, 4));
        int[] yPoints5 = {67, 99, 67, 99};
        int[] xPoints5 = {34, 66, 34, 66};
        zones.set(5, new Polygon(xPoints5, yPoints5, 4));
        int[] yPoints6 = {33, 0, 33, 0};
        int[] xPoints6 = {67, 99, 67, 99};
        zones.set(6, new Polygon(xPoints6, yPoints6, 4));
        int[] yPoints7 = {34, 66, 34, 66};
        int[] xPoints7 = {67, 99, 67, 99};
        zones.set(7, new Polygon(xPoints7, yPoints7, 4));
        int[] yPoints8 = {67, 99, 67, 99};
        int[] xPoints8 = {67, 99, 67, 99};
        zones.set(8, new Polygon(xPoints8, yPoints8, 4));
        return zones;
    }
}
