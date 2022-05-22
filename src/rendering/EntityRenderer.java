package rendering;

import entityimpl2.structure.AbstractEntity;
import map.level.Level;
import map.level.table.GameTable;

import java.awt.*;
import java.util.List;

public class EntityRenderer implements Renderer {

    private final AbstractEntity self;
    private boolean shown = false;

    public EntityRenderer(AbstractEntity self) {
        this.self = self;
    }

    @Override
    public void render(GameTable table) {
        if (shown()) {
            table.add(self.graphic(), self.location());
            table.setColor(self.location(), self.tileColor());
        }
    }

    @Override
    public void addShownPoints(List<Point> points) {
        // do nothing
    }

    @Override
    public List<Point> getShownPoints() {
        return null;
    }

    @Override
    public void reveal() {
        shown = true;
    }

    @Override
    public boolean shown() {
        return shown || Level.getLevel().shouldRender(self.location());
    }
}
