package map.level;

import map.RoomTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Room {

    private static ArrayList<Room> rooms = new ArrayList<>();

    private Polygon polygon;

    private Point center;
    private RoomTableModel model;
    private Dimension size;

    public Room(RoomTableModel model, int x, int y, Dimension size) {
        createBounds(x, y, size);

        this.model = model;
        center = new Point(x, y);
        this.size = size;

        addRoom();
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
    public static boolean checkValidSpace(int x, int y, Dimension size) {
        Rectangle tempBounds = new Rectangle(x, y, (int) size.getWidth(), (int) size.getHeight());
        for (Room room : rooms ) {
            if (room.getBounds().contains(tempBounds)) {
                return false;
            }
        }
        return true;
    }
}
