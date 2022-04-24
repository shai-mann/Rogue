package rendering;

import map.level.table.CustomRoomTable;

import java.awt.*;
import java.util.Collections;

public class DoorRenderer extends AbstractRenderer implements Renderable {

    public static final String DOOR_TILE = "+";

    public DoorRenderer(Point p, boolean shown) {
        super(Collections.singletonList(p));
        if (shown) reveal();
    }

    @Override
    protected String getTile(Point p) {
        return DOOR_TILE;
    }

    @Override
    public void render(CustomRoomTable table) {
        if (shown) table.add(DOOR_TILE, points.get(0));
    }

    public boolean isShown() {
        return shown;
    }
}
