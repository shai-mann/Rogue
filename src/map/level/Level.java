package map.level;

import entity.Entity;
import entity.lifelessentity.Staircase;
import entity.lifelessentity.item.*;
import entity.lifelessentity.item.combat.Armor;
import entity.livingentity.Monster;
import entity.livingentity.Player;
import helper.Helper;
import main.GameManager;
import map.level.table.CustomCellRenderer;
import map.level.table.CustomRoomTable;
import map.level.table.RoomTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Level extends JComponent {

    /*
    * LEVEL CLASS:
    * The Level class creates a new JTable the "level", and then generates rooms on it
    * Generates 5-8 rooms per level
    * Generates Passageways connecting all rooms to all other rooms
    * Generates Treasure and monsters in each of the rooms
    * Generates the Player in the starting table with the descendingStaircase up one level in the same table
    * Generates descendingStaircase going down to next level (except on last level)
    * Level size is 69 * 40
     */

    private JPanel panel;
    private CustomRoomTable table;
    private Room startingRoom;
    private Staircase descendingStaircase;
    private Staircase ascendingStaircase;
    private int levelNumber = 0;

    private static Level level;

    public Level() {
        setDefaults();
        newLevel(Player.DOWN);
    }
    public void newLevel(int direction) {
        generateLevel(direction);
        spawnEntities(direction);
        panel.revalidate();
        panel.repaint();
        GameManager.getFrame().requestFocus();
    }
    private void generateLevel(int direction) {
        if (levelNumber != 0 && direction == Player.DOWN) {
            resetTable();
        }
        if (direction == Player.DOWN) {
            levelNumber++;
        } else {
            levelNumber--;
        }
        generateRooms();
        startingRoom = generatePassageways();
    }

    // PHYSICAL LEVEL GENERATION METHODS

    private void generateRooms() {
        int roomNumber = Helper.random.nextInt(4) + 5;
        for (int i = 0; i < roomNumber; i++) {
            Dimension size = getRandomRoomSize();
            Point point;
            do {
                point = getRandomPoint();
                if (!checkValidPoint(point) || !Room.checkValidSpace(point.x, point.y, size)) {
                    point = null;
                } else {
                    new Room(point, size);
                }
            } while (point == null);
        }
    }
    private Room generatePassageways() {
        /*
        * 1) Get closest unconnected table to top left
        * 2) Get closest connected table to that table
        * 3) Connect those rooms
        * 4) Repeat until there are no unconnected rooms
         */
        ArrayList<Room> unconnectedRooms = (ArrayList<Room>) Room.rooms.clone();
        ArrayList<Room> connectedRooms = new ArrayList<>();
        Room room;
        Room closestRoom;

        for (int i = 0; i < unconnectedRooms.size();) {
            room = getClosestRoom(new Point(0,0), unconnectedRooms);
            if (connectedRooms.size() != 0) {
                closestRoom = getClosestRoom(room, connectedRooms);
            } else {
                closestRoom = getClosestRoom(room, unconnectedRooms);
            }
            new Passageway(room, closestRoom);
            connectedRooms.add(room);
            unconnectedRooms.remove(room);
            connectedRooms.add(closestRoom);
            unconnectedRooms.remove(closestRoom);
        }
        return getClosestRoom(new Point(34, 20), connectedRooms);
    }

    // ENTITY SPAWNING METHODS

    private void spawnEntities(int direction) {
        Monster.updateAvailableMonsters();
        Monster.spawnMonsters();
        Item.spawnItems();
        if (levelNumber != 26 && direction == Player.DOWN) {
            descendingStaircase = new Staircase(((Room) Helper.getRandom(Room.rooms)).getRandomPointInBounds(), Player.DOWN);
        }
        if (levelNumber != 1 && direction == Player.UP) {
            ascendingStaircase = new Staircase(((Room) Helper.getRandom(Room.rooms)).getRandomPointInBounds(), Player.UP);
        }
        new Armor(getStartingRoom().getRandomPointInBounds().x, getStartingRoom().getRandomPointInBounds().y);
        // TODO: remove when done testing LoL
    }

    // ROOM GENERATION HELPER METHODS

    private Dimension getRandomRoomSize() {
        return new Dimension(Helper.random.nextInt(7) + 5,
                Helper.random.nextInt(6) + 6);
    }
    private Point getRandomPoint() {
        return new Point(Helper.random.nextInt(GameManager.getTable().getColumnCount() - 1),
                Helper.random.nextInt(GameManager.getTable().getRowCount() - 1));
    }
    private boolean checkValidPoint(Point p) {
        try {
            return GameManager.getTable().getColumnCount() - p.x > 3 &&
                    GameManager.getTable().getRowCount() - p.y > 3 &&
                    p.x > 3 && p.y > 3;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    // PASSAGEWAY GENERATION HELPER METHODS

    private Room getClosestRoom(Room room, ArrayList<Room> rs) {
        ArrayList<Room> rooms = (ArrayList<Room>) rs.clone();
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
    private Room getClosestRoom(Point p, ArrayList<Room> rs) {
        ArrayList<Room> rooms = (ArrayList<Room>) rs.clone();
        Room closestRoom = rooms.get(0);
        for (int i = 0; i < rooms.size(); i++) {
            if (Passageway.getDistance(p, rooms.get(i).getTopLeft()) <
                    Passageway.getDistance(p, closestRoom.getTopLeft())) {
                closestRoom = rooms.get(i);
            }
        }
        return closestRoom;
    }

    // TABLE GENERATION METHODS

    private void setDefaults() {
        panel.setBackground(Helper.BACKGROUND_COLOR);
        panel.setBorder(null);
        panel.setPreferredSize(new Dimension(GameManager.getFrame().getWidth(), (int) (GameManager.getFrame().getHeight() * 0.8)));
        panel.setMaximumSize(new Dimension(GameManager.getFrame().getWidth(), (int) (GameManager.getFrame().getHeight() * 0.8)));
        panel.setMinimumSize(new Dimension(GameManager.getFrame().getWidth(), (int) (GameManager.getFrame().getHeight() * 0.8)));
        table.setBackground(Helper.BACKGROUND_COLOR);
        table.setGridColor(Helper.BACKGROUND_COLOR);
        table.setRowHeight((int) (table.getRowHeight() * 0.9));

        level = this;
    }
    private void createUIComponents() {
        table = new CustomRoomTable(createTableModel());

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
    private void resetTable() {
        Monster.getMonsters().clear();
        Item.items.clear();
        Room.rooms.clear();
        Door.getDoors().clear();
        descendingStaircase = null;
        ascendingStaircase = null;
        for (int i = 0; i < table.getRowCount(); i++) {
            for (int j = 0; j < table.getColumnCount(); j++) {
                table.setValueAt("", i, j);
            }
        }
    }

    // GETTER METHODS

    public CustomRoomTable getTable() {
        return table;
    }
    public static Level getLevel() {
        return level;
    }
    public Room getStartingRoom() {
        return startingRoom;
    }
    public Room getRoom(Entity entity) {
        for (Room room : Room.rooms) {
            if (room.getBounds().contains(new Point(entity.getXPos(), entity.getYPos()))) {
                return room;
            }
        }
        return null;
    }
    public int getLevelNumber() {
        return levelNumber;
    }
    public Staircase getStaircase() {
        if (descendingStaircase == null) {
            return ascendingStaircase;
        } else {
            return descendingStaircase;
        }
    }
}
