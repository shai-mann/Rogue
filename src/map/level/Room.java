package map.level;

import map.level.table.GameTable;
import rendering.AbstractRenderedModel;
import rendering.Renderer;
import rendering.RoomRenderer;
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
        rooms.add(this);

        attributes = new RoomAttributes(topLeft, size);
        renderer = new RoomRenderer(attributes);
    }

    public Point createDoor(Point destination) {
        List<Point> wallPoints = attributes.getPoints().stream().filter(
                (p) -> p.x == getTopLeft().x ^ p.y == getTopLeft().y ^
                        p.x == getTopLeft().x + getSize().width ^ p.y == getTopLeft().y + getSize().height
        ).filter(GameTable::isNotAgainstEdge).toList();

        // filter out all but X closest points
        wallPoints = wallPoints.stream().sorted(
                Comparator.comparingDouble(p -> p.distance(destination))
        ).limit(10).toList();

        Point doorLocation = Helper.getRandom(wallPoints);
        int hiddenDoorChance = Level.getLevel().getLevelNumber() > 11 ? 50 : 3 * Level.getLevel().getLevelNumber();

        doors.add(new Door(doorLocation, !Helper.calculateChance(hiddenDoorChance / 100.0)));

        return doorLocation;
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
    public Point getRandomPointInBounds() {
        return Helper.getRandom(attributes.getPoints());
        // todo: prevent from overlapping with other entities when spawning
    }
}
