package rendering;

import map.level.table.GameTable;
import util.Helper;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractRenderer implements Renderer {

    protected final List<Point> points;
    protected final Set<Point> shownPoints = new HashSet<>();
    protected boolean shown = false;

    public AbstractRenderer(List<Point> points) {
        this.points = points;
    }

    protected abstract String getTile(Point p);

    protected Color getTileColor() {
        return Helper.FOREGROUND_COLOR;
    }

    @Override
    public void render(GameTable table) {
        for (Point p : shown ? points : shownPoints) {
            table.add(getTile(p), p);
            table.setColor(p, getTileColor());
        }
    }

    @Override
    public void addShownPoints(List<Point> p) {
        this.shownPoints.addAll(p.stream().filter(this.points::contains).toList());
    }

    @Override
    public List<Point> getShownPoints() {
        return (shown ? points : shownPoints).stream().toList();
    }

    @Override
    public void reveal() {
        shown = true;
    }

    @Override
    public boolean shown() {
        return shown;
    }
}
