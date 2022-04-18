package map.level;

import entity.Entity;
import entity.lifelessentity.Staircase;
import entity.lifelessentity.item.Item;
import entity.lifelessentity.item.combat.Armor;
import entity.lifelessentity.item.combat.Weapon;
import entity.livingentity.Player;
import entity.livingentity.monster.Monster;
import main.GameManager;
import map.level.table.CustomCellRenderer;
import map.level.table.CustomRoomTable;
import map.level.table.RoomTableModel;
import org.jetbrains.annotations.Nullable;
import util.Helper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

public class Level extends JComponent {

    /*
    * LEVEL CLASS:
    * The Level class creates a new JTable the "level", and then generates rooms on it
    * Generates 5-8 rooms per level
    * Generates Passageways connecting all rooms to all other rooms
    * Generates Treasure and data.monsters in each of the rooms
    * Generates the Player in the starting hiddenTable with the descendingStaircase up one level in the same hiddenTable
    * Generates descendingStaircase going down to next level (except on last level)
    * Level size is 69 * 40
     */

    private JPanel panel;
    private CustomRoomTable table;
    private CustomRoomTable hiddenTable;
    private Room startingRoom;
    private Staircase descendingStaircase;
    private Staircase ascendingStaircase;
    private int levelNumber = 0;

    private ArrayList<Point> shownPoints = new ArrayList<>();
    private ArrayList<Point> blindnessPoints = new ArrayList<>();

    private static Level level;

    public Level() {
        setDefaults();
        newLevel(true);
    }

    /**
     * Used to generate a {@link Level} that only contains one room. Generally used
     * to test various monster/player/item related features.
     * @param generateAsTestLevel parameter value is ignored - only used to distinguish between other constructors.
     */
    public Level(boolean generateAsTestLevel) {
        setDefaults();
        startingRoom = new Room(new Point(19, 5), new Dimension(15, 15));

        new Monster(Level.class.getClassLoader().getResource("./data/monsters/zombie.txt").getPath(), 20, 6);
        GameManager.getFrame().requestFocus();
    }

    public Level(CustomRoomTable hiddenTable,
                 CustomRoomTable shownTable,
                 Staircase staircase,
                 int direction,
                 int levelNumber,
                 Room startingRoom,
                 ArrayList<Point> shownPoints,
                 ArrayList<Point> blindnessPoints) {
        setDefaults();
        this.hiddenTable = hiddenTable;
        this.table = shownTable;
        if (levelNumber != 26 && direction == Player.DOWN) {
            descendingStaircase = staircase;
        } else {
            ascendingStaircase = staircase;
        }
        this.startingRoom = startingRoom;
        this.shownPoints = shownPoints;
        this.blindnessPoints = blindnessPoints;

        panel.add(table);

        panel.revalidate(); // TODO: confirm that these are unnecessary?
        panel.repaint();
        GameManager.getFrame().requestFocus();
    }

    public void newLevel(boolean descending) {
        if (descending) {
            descendLevel();
        } else {
            ascendLevel();
        }

        spawnEntities();
        panel.revalidate();
        panel.repaint();
        GameManager.getFrame().requestFocus();
        shownPoints.clear();
    }

    private void descendLevel() {
        levelNumber++;
        generateLevel();
    }

    private void ascendLevel() {
        levelNumber--;
        generateLevel();
    }

    private void generateLevel() {
        if (levelNumber != 0) {
            resetTable();
        }

        generateRooms(Helper.getRandom(5, 8)); // TODO: convert from hardcoded literals
        startingRoom = generatePassageways();
    }

    public void update() {
        Point location = GameManager.getPlayer().getLocation();

        if (GameManager.getPlayer().getStatus().isBlinded()) {
            blind();
        }
        if (levelNumber > 5 || GameManager.getPlayer().overWrittenGraphic.equals("#")
                || GameManager.getPlayer().overWrittenGraphic.equals("+")) {
            addShownPoint(location);
            addShownPoint(new Point(location.x, location.y + 1));
            addShownPoint(new Point(location.x, location.y - 1));
            addShownPoint(new Point(location.x + 1, location.y));
            addShownPoint(new Point(location.x - 1, location.y));
        } else if (GameManager.getPlayer() != null){
            for (Point p: getRoom(GameManager.getPlayer()).getRoomPoints()) {
                addShownPoint(p);
            }
        }

        for (Point p : shownPoints) {
            table.setValueAt(hiddenTable.getValueAt(p.y, p.x), p.y, p.x);
        }
    }

    // UPDATE HELPER METHODS

    public void addShownPoint(Point p) {
        for (Point point : shownPoints) {
            if (p.getY() == point.getY() && p.getX() == point.getX()) {
                return;
            }
        }
        shownPoints.add(p);
    }
    public void finalSetup() {
        Point p = GameManager.getPlayer().getLocation();
        addShownPoint(p);
        table.setValueAt(hiddenTable.getValueAt(p.y, p.x), p.y, p.x);
    }
    public void blind() {
        // TODO: make the player visible when blinded
        blindnessPoints.addAll(shownPoints);
        shownPoints.clear();

        for (int i = 0; i < hiddenTable.getRowCount(); i++) {
            for (int j = 0; j < hiddenTable.getColumnCount(); j++) {
                table.setValueAt("", i, j);
            }
        }
    }
    public void unblind() {
        shownPoints.addAll(blindnessPoints);
        blindnessPoints.clear();
    }

    // PHYSICAL LEVEL GENERATION METHODS

    private void generateRooms(int numRooms) {
        for (int i = 0; i < numRooms; i++) {
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
        * 1) Get closest unconnected hiddenTable to top left
        * 2) Get closest connected hiddenTable to that hiddenTable
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

    private void spawnEntities() {
        Monster.updateAvailableMonsters();
        Monster.spawnMonsters();
        Item.spawnItems();
        if (levelNumber != 26) {
            descendingStaircase = new Staircase((Helper.getRandom(Room.rooms)).getRandomPointInBounds(), Player.DOWN);
        }
        if (levelNumber != 1) {
            ascendingStaircase = new Staircase((Helper.getRandom(Room.rooms)).getRandomPointInBounds(), Player.UP);
        }
        if (levelNumber == 1) {
            // give player mace
            spawnStartingPackItem("/data/weapons/mace", null);
            spawnStartingPackItem(null, Item.itemTypes.ARMOR);
        }
    }

    private void spawnStartingPackItem(@Nullable String itemPath, Item.itemTypes type) {
        Point p = getStartingRoom().getRandomPointInBounds();
        if (type != null) {
            Item i = Item.spawnItem(p, type);
            if (i instanceof Armor) {
                ((Armor) i).setAc(8);
                ((Armor) i).setName("Leather Armor");
                ((Armor) i).setCursed(false);
            }
        } else if (itemPath != null){
            try {
                if (GameManager.notJAR) {
                    new Weapon("./resources" + itemPath, p.x, p.y);
                } else {
                    new Weapon(new File(Weapon.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent()
                            + itemPath, p.x, p.y);
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
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
        panel.setSize(new Dimension(GameManager.getFrame().getWidth(), (int) (GameManager.getFrame().getHeight() * 0.8)));
        table.setBackground(Helper.BACKGROUND_COLOR);
        table.setGridColor(Helper.BACKGROUND_COLOR);
        table.setRowHeight(panel.getHeight() / table.getRowCount());
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setWidth(panel.getWidth() / table.getColumnCount());
        }


        level = this;
    }
    private void createUIComponents() {
        hiddenTable = new CustomRoomTable(createTableModel(), false);
        table = new CustomRoomTable(createTableModel(), false);

        table.setFocusable(false);
        table.setRowSelectionAllowed(false);
        table.setForeground(Helper.BACKGROUND_COLOR);
        table.setGridColor(Helper.BACKGROUND_COLOR);
        table.setFont(new Font(Helper.THEME_FONT, Font.BOLD, 12));

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new CustomCellRenderer());
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
        for (int i = 0; i < hiddenTable.getRowCount(); i++) {
            for (int j = 0; j < hiddenTable.getColumnCount(); j++) {
                hiddenTable.setValueAt("", i, j);
                table.setValueAt("", i, j);
            }
        }
    }

    // GETTER METHODS

    public CustomRoomTable getHiddenTable() {
        return hiddenTable;
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
    public ArrayList<Point> getShownPoints() {
        return shownPoints;
    }
    public ArrayList<Point> getBlindnessPoints() {
        return blindnessPoints;
    }
    public CustomRoomTable getShownTable() {
        return table;
    }
    public int getDirection() {
        return ascendingStaircase == null && levelNumber < 26 ? Player.DOWN : Player.UP;
    }
}
