package rendering.structure;

import map.level.table.GameTable;
import rendering.structure.Renderer;

import java.awt.*;
import java.util.List;

public abstract class AbstractRenderedModel implements Renderer {

    protected abstract Renderer renderer();

    @Override
    public void addShownPoints(List<Point> points) {
        renderer().addShownPoints(points);
    }

    @Override
    public List<Point> getShownPoints() {
        return renderer().getShownPoints();
    }

    @Override
    public void reveal() {
        renderer().reveal();
    }

    @Override
    public boolean shown() {
        return renderer().shown();
    }

    @Override
    public void render(GameTable table) {
        renderer().render(table);
    }
}
