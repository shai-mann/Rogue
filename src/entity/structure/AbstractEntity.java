package entity.structure;

import map.level.table.GameTable;
import rendering.EntityRenderer;
import rendering.Renderer;

import java.awt.*;
import java.util.List;

public abstract class AbstractEntity implements Renderer, Entity {

    private final EntityProperties properties;
    private final Renderer renderer;
    protected Point location;

    public AbstractEntity(EntityProperties properties, Point location) {
        this.properties = properties;
        this.renderer = new EntityRenderer(this);
        this.location = location;
    }

    /* OVERRIDES */

    @Override
    public Point location() {
        return location;
    }

    @Override
    public String name() {
        return properties.name();
    }

    @Override
    public String graphic() {
        return properties.graphic();
    }

    @Override
    public Color tileColor() {
        return properties.graphicColor();
    }

    /* RENDERING */

    @Override
    public void render(GameTable table) {
        renderer.render(table);
    }

    @Override
    public void addShownPoints(List<Point> points) {
        renderer.addShownPoints(points);
    }

    @Override
    public List<Point> getShownPoints() {
        return renderer.getShownPoints();
    }

    @Override
    public void reveal() {
        renderer.reveal();
    }

    @Override
    public boolean shown() {
        return renderer.shown();
    }
}
