package rendering;

import map.level.RoomAttributes;

import java.awt.*;

public class RoomRenderer extends AbstractRenderer implements Renderer {

    public static final String HORIZONTAL_WALL_TILE = "=";
    public static final String VERTICAL_WALL_TILE = "|";
    public static final String FLOOR_TILE = "-";

    private final RoomAttributes roomAttrs;

    public RoomRenderer(RoomAttributes roomAttrs) {
        super(roomAttrs.getPoints());
        this.roomAttrs = roomAttrs;
    }

    protected String getTile(Point p) {
        if (isVerticalWall(p)) {
            return VERTICAL_WALL_TILE; // corners default to vertical tile
        } else if (isHorizontalWall(p)) {
            return HORIZONTAL_WALL_TILE;
        }

        return FLOOR_TILE;
    }

    private boolean isVerticalWall(Point p) {
        return p.x == roomAttrs.x() || p.x == roomAttrs.x() + roomAttrs.width();
    }

    private boolean isHorizontalWall(Point p) {
        return p.y == roomAttrs.y() || p.y == roomAttrs.y() + roomAttrs.height();
    }

}
