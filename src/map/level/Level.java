package map.level;

import entity.Entity;
import entity.lifelessentity.Staircase;
import entity.lifelessentity.item.Item;
import entity.lifelessentity.item.combat.Armor;
import entity.lifelessentity.item.combat.Weapon;
import entity.livingentity.Player;
import entity.livingentity.monster.Monster;
import main.GameManager;
import map.level.table.CustomRoomTable;
import map.level.table.GameTable;
import map.level.table.RoomTableModel;
import org.jetbrains.annotations.Nullable;
import rendering.Renderer;
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
    private JTable displayTable;

    private GameTable gameTable;
    private CustomRoomTable hiddenTable; // todo: remove
    private Room startingRoom;
    private Staircase descendingStaircase;
    private Staircase ascendingStaircase;
    private int levelNumber = 0;

    private final Set<Point> shownPoints = new HashSet<>();

    private final List<Room> rooms = new ArrayList<>();
    private final List<Passageway> passageways = new ArrayList<>();
    private final List<Renderer> renderables = new ArrayList<>();

    private static Level level;

    public Level() {
        setDefaults();
        newLevel(true);
    }

    public Level(CustomRoomTable hiddenTable,
                 CustomRoomTable shownTable,
                 Staircase staircase,
                 int direction,
                 int levelNumber,
                 Room startingRoom) {
        setDefaults();
        this.hiddenTable = hiddenTable;
//        this.table = shownTable;
        if (levelNumber != 26 && direction == Player.DOWN) {
            descendingStaircase = staircase;
        } else {
            ascendingStaircase = staircase;
        }
        this.startingRoom = startingRoom;

        panel.add(gameTable.getPanel()); // TODO: necessary?

        panel.revalidate(); // TODO: confirm that these are unnecessary?
        panel.repaint();
    }

    public void newLevel(boolean descending) {
        levelNumber += descending ? 1 : -1;
        generateLevel();

//        spawnEntities();
        panel.revalidate();
        panel.repaint();
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

        gameTable.clear();

        for (Renderer renderer : renderables) {
            if (GameManager.getPlayer().getStatus().isBlinded() && !(renderer instanceof Player)) {
                continue;
            }

            renderer.render(gameTable);
        }
    }

    private void addShownPoints() throws Exception {
        Player player = GameManager.getPlayer();

        List<Point> newPoints = Helper.getAdjacentPoints(player.getLocation(), true);

        Renderer renderer = getRenderableContainingPlayer(player);

        if (levelNumber > 5 || renderer instanceof Passageway) {
            renderer.addShownPoints(newPoints);
            shownPoints.addAll(newPoints);
        } else {
            renderer.reveal();
            shownPoints.addAll(renderer.getShownPoints());
        }
    }

    private Renderer getRenderableContainingPlayer(Player player) throws Exception {
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
//        new Monster(MonsterLoader.monsterClasses.get(25), 20, 6);

        render(); // renders the first room the player is in
    }

    // PHYSICAL LEVEL GENERATION METHODS

    private void generateRooms(int numRooms) {
        rooms.addAll(LevelGenerator.generate(numRooms));
        renderables.addAll(rooms);
    }

    private Room generatePassageways() {
        passageways.addAll(LevelGenerator.generate(rooms));
        renderables.addAll(passageways);

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
        } else if (itemPath != null) {
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

        level = this;
    }

    private void createUIComponents() {
        hiddenTable = new CustomRoomTable(createTableModel(), false);
        gameTable = new GameTable();
        displayTable = gameTable.getTable();
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

    public CustomRoomTable getTable() {
        return hiddenTable; // todo: change to gameTable
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

    public int getDirection() {
        return ascendingStaircase == null && levelNumber < 26 ? Player.DOWN : Player.UP;
    }

    public Set<Point> getShownPoints() {
        return shownPoints;
    }

}
