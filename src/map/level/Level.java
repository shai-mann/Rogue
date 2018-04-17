package map.level;

import helper.Helper;
import main.GameManager;
import map.CustomCellRenderer;
import map.CustomRoomTable;
import map.RoomTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Level extends JComponent {

    /*
    * LEVEL CLASS:
    * The Level class is made of a grid (represented by a JTable) which things such as the player and monsters
    * can be added to
    * Rooms can only be from 11 length to 5
    * Rooms must be odd lengths so as to have a center (Not requirement of room)
     */

    private JPanel panel;
    private CustomRoomTable table;
    private Random random = new Random();
    private ArrayList zones;

    private static Level level;

    public Level() {
        level = this;

        Room.zones = Room.setZones();
        zones = (ArrayList) Room.zones.clone();

        setDefaults();

        createRooms();
        createPassageways();
    }
    /*
    * Level size is 69 x 40
     */
    private void createRooms() {
        int roomNumber = random.nextInt(2) + 4;

        for (int i = 0; i < roomNumber; i++) {
            Point point;
            Dimension size;
            Polygon zone;
            do {
                zone = (Polygon) zones.get(random.nextInt(zones.size() - 1));
                point = getRandomPoint(zone);
                try {
                    size = getRandomRoomSize(point);
                } catch (Exception e) {
                    size = null;
                }
            } while(size == null);
            new Room(table.getCustomModel(), point, size);
            zones.remove(zone);
        }
    }
    private void createPassageways() {
        // TODO: Create Passageway generation
        // 1) Start in one room, and mark that room as starting room
        // 2) Pick random room as end destination
        // 3) Backtrack and use original room to go to different room
        // 4) Update copy of room list to remove starting room from list
        // 5) First of two ending destinations becomes the starting room
        // 6) Repeat until all rooms have at least one door (create method to check)
        ArrayList<Room> rooms = (ArrayList) Room.rooms.clone();

        Room startingRoom = rooms.get(random.nextInt(Room.rooms.size() - 1));
        rooms.remove(startingRoom);

        while (!checkForDoorInEachRoom() && !(rooms.size() <= 1)) {
            int iterations = random.nextInt(2) + 1;
            for (int i = 0; i < iterations; i++) {
                Room end = rooms.get(random.nextInt(rooms.size() - 1));
                rooms.remove(end);
                new Passageway(startingRoom, end);
                startingRoom = end;
            }
        }
    }
    private boolean checkForDoorInEachRoom() {
        int roomsCompleted = 0;

        for (Room room : Room.rooms) {
            if (room.doors.size() > 0) {
                roomsCompleted++;
            }
        }
        return roomsCompleted == Room.rooms.size();
    }
    private Point getRandomPoint(Polygon zone) {
        Point point;
        do {
            point = new Point(random.nextInt(zone.getBounds().width - 1) + (int) zone.getBounds().getMinX(),
                    random.nextInt(zone.getBounds().height - 1) + (int) zone.getBounds().getMinY());

            if (checkValidPoint(point)) {
                return point;
            } else {
                point = null;
            }
        } while (point == null);
        return point;
    }
    private Dimension getRandomRoomSize(Point p) throws Exception {
        Dimension dim;
        int iterations = 0;

        do {
            dim = new Dimension(random.nextInt(6) + 6, random.nextInt(6) + 6);
            dim.width = (int) (Math.floor(dim.width / 2) * 2) - 1;
            dim.height = (int) (Math.floor(dim.height / 2)  * 2) - 1;
            if (Room.checkValidSpace(p.x, p.y, dim)) {
                return dim;
            } else {
                dim = null;
            }
            iterations++;

            if (iterations == 10) {
                throw new Exception("Entered infinite loop");
            }
        } while (dim == null);
        return dim;
    }
    private boolean checkValidPoint(Point p) {
        try {
            return GameManager.getTable().getValueAt(p.y, p.x) == "";
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }
    private void setDefaults() {
        panel.setBackground(Helper.BACKGROUND_COLOR);
        panel.setPreferredSize(new Dimension(GameManager.getFrame().getWidth(), (int) (GameManager.getFrame().getHeight() * 0.9)));
        panel.setMaximumSize(new Dimension(GameManager.getFrame().getWidth(), (int) (GameManager.getFrame().getHeight() * 0.9)));
        panel.setMinimumSize(new Dimension(GameManager.getFrame().getWidth(), (int) (GameManager.getFrame().getHeight() * 0.9)));
        panel.setBorder(null);
        table.setBackground(Helper.BACKGROUND_COLOR);
        table.setGridColor(Helper.BACKGROUND_COLOR);
    }
    private void createUIComponents() {
        RoomTableModel model = createTableModel();
        table = new CustomRoomTable(model);

        table.setFocusable(false);
        table.setRowSelectionAllowed(false);
        table.setForeground(Helper.BACKGROUND_COLOR);
        table.setGridColor(Helper.BACKGROUND_COLOR);
        table.setFont(new Font(Helper.THEME_FONT, Font.BOLD, 12));

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new CustomCellRenderer());
            table.getColumnModel().getColumn(i).setMinWidth(20);
            table.getColumnModel().getColumn(i).setMaxWidth(20);
        }
    }
    private RoomTableModel createTableModel() {
        RoomTableModel model = new RoomTableModel();
        for (int j = 0; j != 69; j++) {
            model.addColumn("");
        }
        for (int i = 0; i != 40; i++) {
            model.addRow(createTableRow());
        }
        return model;
    }
    private String[] createTableRow() {
        String[] rowValueList = new String[69];
        Arrays.fill(rowValueList, "");

        return rowValueList;
    }
    public CustomRoomTable getTable() {
        return table;
    }
    public static Level getLevel() {
        return level;
    }
}
