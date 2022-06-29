package map.level;

import rendering.structure.AbstractRenderedModel;
import rendering.structure.Renderer;
import rendering.level.DoorRenderer;

import java.awt.*;
import java.util.ArrayList;

public class Door extends AbstractRenderedModel implements Renderer {

    private static final ArrayList<Door> doors = new ArrayList<>(); // TODO: remove

    private final DoorRenderer renderer;
    private final Point location;

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
