package map.level;

import main.GameManager;
import util.Helper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LevelGenerator {

    public static List<Room> generate(int numRooms) {
        List<Room> rooms = new ArrayList<>();

        for (int i = 0; i < numRooms; i++) {
            Dimension size = getRandomRoomSize();
            Point point;
            do {
                point = getRandomPoint();
                if (isValidRoom(point, size, rooms)) {
                    rooms.add(new Room(point, size));
                } else {
                    point = null;
                }
            } while (point == null);
        }

        return rooms;
    }

    public static List<Passageway> generate(List<Room> rooms) {
        // todo: make not mutate list given lmaoooo
        List<Passageway> passageways = new ArrayList<>();

        /*
         * 1) Get closest unconnected hiddenTable to top left
         * 2) Get closest connected hiddenTable to that hiddenTable
         * 3) Connect those rooms
         * 4) Repeat until there are no unconnected rooms
         */
        ArrayList<Room> connectedRooms = new ArrayList<>();
        Room room;
        Room closestRoom;

        for (int i = 0; i < rooms.size();) {
            room = getClosestRoom(new Point(0,0), rooms);
            closestRoom = getClosestRoom(room, connectedRooms.size() != 0 ? connectedRooms : rooms);

            passageways.add(new Passageway(room, closestRoom));
            connectedRooms.add(room);
            connectedRooms.add(closestRoom);
            rooms.remove(closestRoom);
        }
//        return getClosestRoom(new Point(34, 20), connectedRooms);
        return passageways;
    }

    /* HELPERS */

    private static Dimension getRandomRoomSize() {
        return new Dimension(Helper.random.nextInt(7) + 5,
                Helper.random.nextInt(6) + 6);
    }

    private static Point getRandomPoint() {
        return new Point(Helper.random.nextInt(GameManager.getTable().getColumnCount() - 1),
                Helper.random.nextInt(GameManager.getTable().getRowCount() - 1));
    }

    private static boolean isValidRoom(Point p, Dimension size, List<Room> rooms) {
        if (!GameManager.getTable().getBounds().contains(new Rectangle(p, size))) return false;

        for (Room room : rooms) {
            if (room.bounds().intersects(new Rectangle(p, size))) return false;
        }

        return true;
    }

    private static Room getClosestRoom(Room room, List<Room> rs) {
        List<Room> rooms = new ArrayList<>(rs);
        rooms.remove(room);
        Room closestRoom = rooms.get(0);
        for (int i = 1; i < rooms.size(); i++) {
            if (Passageway.getDistance(room.getTopLeft(), rooms.get(i).getTopLeft()) <
                    Passageway.getDistance(room.getTopLeft(), closestRoom.getTopLeft())) {
                closestRoom = rooms.get(i);
            }
        }
        return closestRoom;
    }
    private static Room getClosestRoom(Point p, List<Room> rs) {
        List<Room> rooms = new ArrayList<>(rs);
        Room closestRoom = rooms.get(0);
        for (Room room : rooms) {
            if (Passageway.getDistance(p, room.getTopLeft()) <
                    Passageway.getDistance(p, closestRoom.getTopLeft())) {
                closestRoom = room;
            }
        }
        return closestRoom;
    }

}
