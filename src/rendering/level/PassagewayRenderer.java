package rendering.level;

import rendering.structure.AbstractRenderer;
import rendering.structure.Renderer;

import java.awt.*;
import java.util.List;

public class PassagewayRenderer extends AbstractRenderer implements Renderer {

    public static final String HALLWAY_TILE = "#";

    public PassagewayRenderer(List<Point> points) {
        super(points);
    }

    protected String getTile(Point p) {
        return HALLWAY_TILE;
    }

}
