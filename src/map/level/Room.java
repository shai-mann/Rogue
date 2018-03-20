package map.level;

import map.RoomTableModel;
import java.awt.*;
import java.util.ArrayList;

public class Room {

    private static ArrayList<Room> rooms = new ArrayList<>();

    private Polygon polygon;

    public Room(RoomTableModel model, int x, int y, Dimension size) {
        createBounds(x, y, size);
    }
    public Polygon getBounds() {
        return polygon;
    }
    private void createBounds(int x, int y, Dimension size) {
        int[] xPoints = {(int) (x + size.getWidth()), (int) (x - size.getWidth())};
        int[] yPoints = {(int) (y + size.getWidth()), (int) (y - size.getWidth())};

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
