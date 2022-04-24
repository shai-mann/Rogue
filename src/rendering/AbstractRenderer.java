package rendering;

import map.level.table.CustomRoomTable;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractRenderer implements Renderable {

    protected final List<Point> points;
    protected final Set<Point> shownPoints = new HashSet<>();
    protected boolean shown = false;

    public AbstractRenderer(List<Point> points) {
        this.points = points;
    }

    protected abstract String getTile(Point p);

    @Override
    public void render(CustomRoomTable table) {
        for (Point p : shown ? points : shownPoints) {
            table.add(getTile(p), p);
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
}
