package map.level;

import main.GameManager;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Room {

    public static ArrayList<Room> rooms = new ArrayList<>();
    public ArrayList<Door> doors = new ArrayList<>();
    public ArrayList<Passageway> passageways = new ArrayList<>();

    private Dimension size;
    private Point topLeft;

    public Room(Point p, Dimension size) {
        rooms.add(this);

        this.size = size;
        topLeft = p;

        addRoom();
    }

    // ROOM GENERATION METHODS

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
        int[] xPoints = {x - 3, x + size.width + 3, x + size.width + 3, x - 3};
        int[] yPoints = {y - 3, y - 3, y + size.height + 3, y + size.height + 3};

        Polygon tempBounds = new Polygon(xPoints, yPoints, 4);
        for (Room room : rooms ) {
            int[] xPoints1 = {room.topLeft.x, room.topLeft.x + room.getSize().width,
                    room.topLeft.x + room.getSize().width, room.topLeft.x};
            int[] yPoints1 = {room.topLeft.y, room.topLeft.y,
                    room.topLeft.y + room.getSize().height, room.topLeft.y + room.getSize().height};
            Polygon temp = new Polygon(xPoints1, yPoints1, 4);
            if (temp.intersects(tempBounds.getBounds()) || tempBounds.intersects(temp.getBounds())) {
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

    // GETTER METHODS

    public Point getTopLeft() {
        return topLeft;
    }
    public Dimension getSize() {
        return size;
    }
    public Polygon getBounds() {
        int[] xPoints1 = {topLeft.x, topLeft.x + getSize().width,
                topLeft.x + getSize().width, topLeft.x};
        int[] yPoints1 = {topLeft.y, topLeft.y,
                topLeft.y + getSize().height, topLeft.y + getSize().height};
        return new Polygon(xPoints1, yPoints1, 4);
    }
    public Point getRandomPointInBounds() {
        // should return a random point inside of tbe room (not in the wall tho) NOT FUNCTIONAL
        int minX = (int) getBounds().getBounds().getMinX() + 1;
        int maxX = (int) getBounds().getBounds().getMaxX() - 2;
        int minY = (int) getBounds().getBounds().getMinY() + 1;
        int maxY = (int) getBounds().getBounds().getMaxY() - 2;
        return new Point(new Random().nextInt((maxX - minX) + minX),
                new Random().nextInt(maxY - minY) + minY);
    }
}
