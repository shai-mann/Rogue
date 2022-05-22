package map.level;

import map.level.table.GameTable;
import rendering.AbstractRenderedModel;
import rendering.Renderer;
import rendering.level.RoomRenderer;
import util.Helper;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Room extends AbstractRenderedModel implements Serializable, Renderer {

    public static ArrayList<Room> rooms = new ArrayList<>(); // TODO: remove
    private final ArrayList<Door> doors = new ArrayList<>();

    private final RoomRenderer renderer;
    private final RoomAttributes attributes;

    public Room(Point topLeft, Dimension size) {
        attributes = new RoomAttributes(topLeft, size);
        renderer = new RoomRenderer(attributes);
    }

    public Point createDoor(Point destination) {
        List<Point> wallPoints = attributes.getWallPoints().stream().filter(GameTable::isNotAgainstEdge).toList();

        // filter out all but X closest points
        wallPoints = wallPoints.stream().sorted(
                Comparator.comparingDouble(p -> p.distance(destination))
        ).limit(10).toList();

        Point doorLocation = Helper.getRandom(wallPoints);
        int hiddenDoorChance = Level.getLevel().getLevelNumber() > 11 ? 50 : 3 * Level.getLevel().getLevelNumber();

        doors.add(new Door(doorLocation, !Helper.calculateChance(hiddenDoorChance / 100.0)));

        return doorLocation;
    }

    public boolean canPlaceEntityAt(Point location) {
        return attributes.getNonWallPoints().contains(location);
    }

    public boolean isDoor(Point location) {
        return doors.stream().anyMatch(d -> d.getLocation().equals(location));
    }

    @Override
    protected Renderer renderer() {
        return renderer;
    }

    @Override
    public void render(GameTable table) {
        super.render(table);

        if (!renderer.shown()) return;

        for (Door d : doors) {
            d.render(table);
        }
    }

    // GETTER METHODS

    public Point getTopLeft() {
        return attributes.topLeft();
    }
    public Point getCenter() {
        return Helper.translate(getTopLeft(), new Point(getSize().width / 2, getSize().height / 2));
    }
    public Dimension getSize() {
        return attributes.size();
    }
    public Rectangle bounds() {
        return attributes.bounds();
    }
    public ArrayList<Door> doors() {
        return doors;
    }
    public Point getRandomPointInBounds() {
        return Helper.getRandom(attributes.getNonWallPoints());
        // todo: prevent from overlapping with other entities when spawning
    }
}
