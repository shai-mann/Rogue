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
        rooms.add(this);
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
            if (i == 0 || i == size.getHeight() - 1) {
                rowData = createDefaultRow("=", "=");
            } else {
                rowData = createDefaultRow("|", "-");
            }
            for (int z = 0; z < rowData.length; z++) {
                model.setValueAt(rowData[z], startingPoint.y - i, startingPoint.x - z);
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
        rowValueList[(int) size.getWidth() - 1] = edges;

        return rowValueList;
    }
    private void createBounds(int x, int y, Dimension size) {
        int[] xPoints = {(int) (x + (size.getWidth() / 2)), (int) (x - (size.getWidth() / 2)), (int) (x - (size.getWidth() / 2)), (int) (x + (size.getWidth() / 2))};
        int[] yPoints = {(int) (y + (size.getHeight()) / 2), (int) (y + (size.getHeight()) / 2), (int) (y - (size.getHeight()) / 2), (int) (y - (size.getHeight()) / 2)};

        polygon = new Polygon(xPoints, yPoints, 4);
    }
    private ArrayList<Polygon> setZones() {
        ArrayList<Polygon> zones = new ArrayList<>();
        int[] xPoints = {0, 23, 0, 23};
        int[] yPoints = {23, 0, 23, 0};
        zones.add(0, new Polygon(xPoints, yPoints, 4));
        int[] xPoints1 = {24, 46, 24, 46};
        int[] yPoints1 = {23, 0, 23, 0};
        zones.add(1, new Polygon(xPoints1, yPoints1, 4));
        int[] xPoints2 = {47, 69, 47, 69};
        int[] yPoints2 = {23, 0, 23, 0};
        zones.add(2, new Polygon(xPoints2, yPoints2, 4));
        int[] xPoints3 = {0, 23, 0, 23};
        int[] yPoints3 = {24, 46, 24, 46};
        zones.add(3, new Polygon(xPoints3, yPoints3, 4));
        int[] xPoints4 = {24, 46, 24, 46};
        int[] yPoints4 = {24, 46, 24, 46};
        zones.add(4, new Polygon(xPoints4, yPoints4, 4));
        int[] yPoints5 = {47, 69, 47, 69};
        int[] xPoints5 = {24, 46, 24, 46};
        zones.add(5, new Polygon(xPoints5, yPoints5, 4));
        int[] yPoints6 = {23, 0, 23, 0};
        int[] xPoints6 = {47, 69, 47, 69};
        zones.add(6, new Polygon(xPoints6, yPoints6, 4));
        int[] yPoints7 = {24, 46, 24, 46};
        int[] xPoints7 = {47, 69, 47, 69};
        zones.add(7, new Polygon(xPoints7, yPoints7, 4));
        int[] yPoints8 = {47, 69, 47, 69};
        int[] xPoints8 = {47, 69, 47, 69};
        zones.add(8, new Polygon(xPoints8, yPoints8, 4));
        return zones;
    }
}
