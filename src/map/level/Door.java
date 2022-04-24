package map.level;

import main.GameManager;
import map.level.table.CustomRoomTable;
import rendering.DoorRenderer;
import rendering.Renderable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Door implements Renderable {

    private static final ArrayList<Door> doors = new ArrayList<>(); // TODO: remove

    private final DoorRenderer renderer;
    private final Point location;

    public Door(Point p, boolean shown, String overWrittenGraphic) {
        location = p;
        renderer = new DoorRenderer(location, shown);
        doors.add(this);
        if (isSecret()) {
            GameManager.getTable().setValueAt(overWrittenGraphic, p.y, p.x);
        }
    }

    // GETTER METHODS

    public boolean isSecret() {
        return !renderer.isShown();
    }

    public Point getLocation() {
        return location;
    }

    @Override
    public void render(CustomRoomTable table) {

    }

    @Override
    public void addShownPoints(List<Point> points) {
        renderer.addShownPoints(points);
    }

    @Override
    public List<Point> getShownPoints() {
        return Collections.singletonList(location);
    }

    public void reveal() {
        renderer.reveal();
    }

    public static ArrayList<Door> getDoors() {
        return doors;
    }
}
