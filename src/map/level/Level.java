package map.level;

import entity.component.Status;
import entity.lifeless.Staircase;
import entity.lifeless.item.structure.Item;
import entity.monster.Monster;
import entity.player.Player;
import entity.structure.Entity;
import entity.util.MoveResult;
import main.GameManager;
import map.level.table.CustomRoomTable;
import map.level.table.GameTable;
import map.level.table.RoomTableModel;
import rendering.structure.Renderable;
import rendering.structure.Renderer;
import util.Helper;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

import static entity.lifeless.Staircase.Direction.*;

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
    private final Player player;

    private int levelNumber = 0;

    private final Set<Point> shownPoints = new HashSet<>();

    private final List<Room> rooms = new ArrayList<>();
    private final List<Passageway> passageways = new ArrayList<>();
    private final List<Renderer> renderables = new ArrayList<>();

    private final List<Item> items = new ArrayList<>();
    private final List<Monster> monsters = new ArrayList<>();

    private static Level level;

    public Level() {
        setDefaults();
        newLevel(true);
        player = new Player(startingRoom.getCenter(), this);
        player.apply(panel);
        GameManager.getFrame().requestFocus();

        render();
    }

    // TODO: reimplement saving/loading with overhauled code
//    public Level(CustomRoomTable hiddenTable,
//                 CustomRoomTable shownTable,
//                 Staircase staircase,
//                 int direction,
//                 int levelNumber,
//                 Room startingRoom) {
//        setDefaults();
//        this.hiddenTable = hiddenTable;
////        this.table = shownTable;
//        if (levelNumber != 26 && direction == Player.DOWN) {
//            descendingStaircase = staircase;
//        } else {
//            ascendingStaircase = staircase;
//        }
//        this.startingRoom = startingRoom;
//
//        panel.add(gameTable.getPanel()); // TODO: necessary?
//
//        panel.revalidate(); // TODO: confirm that these are unnecessary?
//        panel.repaint();
//    }

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

    public void update() {
        player.update();
        monsters.forEach(Monster::update);

        render();
    }

    public void render() {
        try {
            addShownPoints(); // take note of where player is and add
        } catch (Exception e) {
            e.printStackTrace();
        }

        gameTable.clear();

        for (Renderable renderable : renderables) {
            if (player.getStatus().is(Status.Stat.BLINDED)) {
                continue;
            } // todo: move if statement outside of for loop

            renderable.render(gameTable);
        }

        player.render(gameTable);
    }

    public boolean shouldRender(Point p) {
        return shownPoints.contains(p);
    }

    private void addShownPoints() throws Exception {
        Player player = getPlayer();

        List<Point> newPoints = Helper.getAdjacentPoints(player.location(), true);

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
            if (room.bounds().contains(player.location())) {
                return room;
            }
        }

        for (Passageway p : passageways) {
            if (p.contains(player.location())) {
                return p;
            }
        }

        throw new Exception("Failed to locate Renderable containing player");
    }

    public MoveResult isValidMove(Entity target, Point displacement) {
        Point newLocation = Helper.translate(target.location(), displacement);

        boolean out = rooms.stream().anyMatch((r) -> r.canPlaceEntityAt(newLocation) || r.isDoor(newLocation));

        out |= passageways.stream().anyMatch((p) -> p.contains(newLocation));

        for (Monster m : monsters) {
            if (newLocation.equals(m.location())) return new MoveResult(m);
        }

        return new MoveResult(out);
    }

    // PHYSICAL LEVEL GENERATION METHODS

    private void generateRooms(int numRooms) {
//        rooms.addAll(LevelGenerator.generate(numRooms));
        rooms.addAll(LevelGenerator.generateTestRoom());
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
//        Item.spawnItems(); // todo: reimplement with new Item/ItemSpawner class
        if (levelNumber != 26) {
            descendingStaircase = new Staircase((Helper.getRandom(Room.rooms)).getRandomPointInBounds(), DOWN);
        }
        if (levelNumber != 1) {
            ascendingStaircase = new Staircase((Helper.getRandom(Room.rooms)).getRandomPointInBounds(), UP);
        }
        if (levelNumber == 1) {
            // give player mace
//            spawnStartingPackItem("/data/weapons/mace", null); // todo: reimplement with new Items
//            spawnStartingPackItem(null, Item.itemTypes.ARMOR);
        }
    }

//    private void spawnStartingPackItem(@Nullable String itemPath, Item.itemTypes type) {
//        Point p = getStartingRoom().getRandomPointInBounds();
//        if (type != null) {
//            Item i = Item.spawnItem(p, type);
//            if (i instanceof Armor) {
//                ((Armor) i).setAc(8);
//                ((Armor) i).setName("Leather Armor");
//                ((Armor) i).setCursed(false);
//            }
//        } else if (itemPath != null) {
//            try {
//                if (GameManager.notJAR) {
//                    new Weapon("./resources" + itemPath, p.x, p.y);
//                } else {
//                    new Weapon(new File(Weapon.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent()
//                            + itemPath, p.x, p.y);
//                }
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//        }
//    }

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
        monsters.clear();
        items.clear();
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
            if (room.bounds().contains(entity.location())) {
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
    public List<Staircase> getStaircases() {
        List<Staircase> staircases = new ArrayList<>();
        if (descendingStaircase != null) staircases.add(descendingStaircase);
        if (ascendingStaircase != null) staircases.add(ascendingStaircase);

        return staircases;
    }

    public Staircase.Direction getDirection() {
        return ascendingStaircase == null && levelNumber < 26 ? DOWN : UP;
    }

    public Set<Point> getShownPoints() {
        return shownPoints;
    }

    public Player getPlayer() {
        return player;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Monster> getMonsters() {
        return monsters;
    }

    public List<Monster> getLoadedMonsters() {
        return getMonsters().stream().filter(Monster::shown).toList();
    }

    public void reveal() {
        for (Renderer r : renderables) {
            r.reveal();
        }
    }
}
