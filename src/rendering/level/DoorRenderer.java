package rendering.level;

import rendering.AbstractRenderer;
import rendering.Renderer;

import java.awt.*;
import java.util.Collections;

public class DoorRenderer extends AbstractRenderer implements Renderer {

    public static final String DOOR_TILE = "+";

    public DoorRenderer(Point p, boolean shown) {
        super(Collections.singletonList(p));
        if (shown) reveal();
    }

    @Override
    protected String getTile(Point p) {
        return DOOR_TILE;
    }

    public boolean isShown() {
        return shown;
    }
}
