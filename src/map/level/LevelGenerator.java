package map.level;

import main.GameManager;
import map.level.table.GameTable;
import util.Helper;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LevelGenerator {

    // default is 70% chance of nearest room, 30% chance random (connected) room
    private static final double CLOSEST_ROOM_CHANCE = 1;

    public static List<Room> generate(int numRooms) {
        List<Room> rooms = new ArrayList<>();

//        rooms.add(new Room(new Point(10, 10), new Dimension(5, 5)));
//        rooms.add(new Room(new Point(30, 30), new Dimension(5, 5)));

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
        /*
        1) Get random room from unconnected
        2) Attach to a connected room (do nothing if no other connected rooms)
        3) Add room to connected rooms list
        4) Repeat until all rooms are in connected list
        5) Have chance to add extra passageways?
         */

        List<Passageway> passageways = new ArrayList<>();
        List<Room> unconnectedRooms = new ArrayList<>(rooms);
        List<Room> connectedRooms = new ArrayList<>();

        while (!unconnectedRooms.isEmpty()) {
            Room origin = Helper.getRandom(unconnectedRooms);

            if (!connectedRooms.isEmpty()) {
                Room endpoint = findEndPoint(origin, connectedRooms);
                passageways.add(new Passageway(origin, endpoint, rooms));
            }

            unconnectedRooms.remove(origin);
            connectedRooms.add(origin);
        }

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
        Rectangle bounds = new Rectangle(p, Helper.translate(size, new Dimension(1, 1)));

        if (!GameTable.isOnTable(bounds)) return false;

        return rooms.stream().noneMatch((r) -> r.bounds().intersects(bounds) || r.bounds().contains(bounds));
    }

    private static Room findEndPoint(Room origin, List<Room> connectedRooms) {
        return Helper.calculateChance(CLOSEST_ROOM_CHANCE) ?
                getClosestRoom(origin, connectedRooms) : Helper.getRandom(connectedRooms);
    }

    /* deprecated */

    /**
     * Returns the closest {@link Room} to the given one in a {@link List} of Rooms. The given room
     * cannot be a in the given list.
     * @param room the room to calculate the closest room to.
     * @param rooms the list of rooms available.
     * @return the closest room to the given one in the given list of rooms.
     */
    private static Room getClosestRoom(Room room, List<Room> rooms) {
        if (rooms.contains(room)) {
            throw new IllegalArgumentException("Given list of rooms contained the target room.");
        }

        Room closestRoom = null;
        double distance = -1;
        for (Room r : rooms) {
            double newDistance = r.getTopLeft().distance(room.getTopLeft());

            if (closestRoom == null || distance > newDistance) {
                closestRoom = r;
                distance = newDistance;
            }
        }

        return closestRoom;
    }

}
