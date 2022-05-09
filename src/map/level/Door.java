package map.level;

import main.GameManager;
import rendering.AbstractRenderedModel;
import rendering.DoorRenderer;
import rendering.Renderer;

import java.awt.*;
import java.util.ArrayList;

public class Door extends AbstractRenderedModel implements Renderer {

    private static final ArrayList<Door> doors = new ArrayList<>(); // TODO: remove

    private final DoorRenderer renderer;
    private final Point location;

    // todo: remove
    public Door(Point p, boolean shown, String overWrittenGraphic) {
        this(p, shown);
        doors.add(this);
        if (isSecret()) {
            GameManager.getTable().setValueAt(overWrittenGraphic, p.y, p.x);
        }
    }

    public Door(Point location, boolean shown) {
        this.location = location;
        renderer = new DoorRenderer(location, shown);
    }

    // GETTER METHODS

    public boolean isSecret() {
        return !renderer.isShown();
    }

    public Point getLocation() {
        return location;
    }

    @Override
    protected Renderer renderer() {
        return renderer;
    }

    // todo: remove
    public static ArrayList<Door> getDoors() {
        return doors;
    }
}
