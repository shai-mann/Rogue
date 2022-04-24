package map.level;

import entity.Entity;
import entity.lifelessentity.Staircase;
import entity.lifelessentity.item.Item;
import entity.lifelessentity.item.combat.Armor;
import entity.lifelessentity.item.combat.Weapon;
import entity.livingentity.Player;
import entity.livingentity.monster.Monster;
import entity.livingentity.monster.MonsterLoader;
import main.GameManager;
import map.level.table.CustomCellRenderer;
import map.level.table.CustomRoomTable;
import map.level.table.RoomTableModel;
import org.jetbrains.annotations.Nullable;
import rendering.Renderable;
import util.Helper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.*;

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

    private final Set<Point> shownPoints = new HashSet<>();

    private final List<Room> rooms = new ArrayList<>();
    private final List<Passageway> passageways = new ArrayList<>();
    private final List<Renderable> renderables = new ArrayList<>();

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

        rooms.add(startingRoom);
    }

    public Level(CustomRoomTable hiddenTable,
                 CustomRoomTable shownTable,
                 Staircase staircase,
                 int direction,
                 int levelNumber,
                 Room startingRoom) {
        setDefaults();
        this.hiddenTable = hiddenTable;
        this.table = shownTable;
        if (levelNumber != 26 && direction == Player.DOWN) {
            descendingStaircase = staircase;
        } else {
            ascendingStaircase = staircase;
        }
        this.startingRoom = startingRoom;

        panel.add(table);

        panel.revalidate(); // TODO: confirm that these are unnecessary?
        panel.repaint();
    }

    public void newLevel(boolean descending) {
        if (descending) {
            descendLevel();
        } else {
            ascendLevel();
        }

//        spawnEntities();
        panel.revalidate();
        panel.repaint();
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

    public void render() {
        try {
            addShownPoints(); // take note of where player is and add
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.clear();

        for (Renderable renderable : renderables) {
            if (GameManager.getPlayer().getStatus().isBlinded() && !(renderable instanceof Player)) {
                continue;
            }

            renderable.render(table);
        }
    }

    private void addShownPoints() throws Exception {
        Player player = GameManager.getPlayer();

        List<Point> newPoints = Helper.getAdjacentPoints(player.getLocation(), true);

        Renderable renderable = getRenderableContainingPlayer(player);

        if (levelNumber > 5 || renderable instanceof Passageway) {
            renderable.addShownPoints(newPoints);
            shownPoints.addAll(newPoints);
        } else {
            renderable.reveal();
            shownPoints.addAll(renderable.getShownPoints());
        }
    }

    private Renderable getRenderableContainingPlayer(Player player) throws Exception {
        for (Room room : rooms) {
            if (room.bounds().contains(player.getLocation())) {
                return room;
            }
        }

        for (Passageway p : passageways) {
            if (p.contains(player.getLocation())) {
                return p;
            }
        }

        throw new Exception("Failed to locate Renderable containing player");
    }

    // UPDATE HELPER METHODS
    public void finalSetup() {
        if (GameManager.bootTestingEnvironment) {
            new Monster(MonsterLoader.monsterClasses.get(25), 20, 6);
        }

        render(); // renders the first room the player is in
    }

    // PHYSICAL LEVEL GENERATION METHODS

    private void generateRooms(int numRooms) {
        rooms.addAll(LevelGenerator.generate(numRooms));
    }

    private Room generatePassageways() {
//        passageways.addAll(LevelGenerator.generate(rooms));

        return Helper.getRandom(rooms);
    }

    // ENTITY SPAWNING METHODS

    private void spawnEntities() {
        Monster.spawnMonsters(levelNumber);
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

    // TABLE GENERATION METHODS

    private void setDefaults() {
        panel.setBackground(Helper.BACKGROUND_COLOR);
        panel.setBorder(null);
        Helper.setSize(panel, new Dimension(GameManager.getFrame().getWidth(), (int) (GameManager.getFrame().getHeight() * 0.8)));
        table.setBackground(Helper.BACKGROUND_COLOR);
        table.setGridColor(Helper.BACKGROUND_COLOR);

        table.setRowHeight(panel.getHeight() / table.getRowCount());
        table.setColumnWidth(panel.getWidth() / table.getColumnCount());

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

        table.setCellRenderer(new CustomCellRenderer());
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
        hiddenTable.clear();
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
            if (room.bounds().contains(new Point(entity.getXPos(), entity.getYPos()))) {
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
    public CustomRoomTable getShownTable() {
        return table;
    }
    public int getDirection() {
        return ascendingStaircase == null && levelNumber < 26 ? Player.DOWN : Player.UP;
    }

    public Set<Point> getShownPoints() {
        return shownPoints;
    }
}
