package rendering;

import map.level.table.CustomRoomTable;

import java.awt.*;
import java.util.List;

public interface Renderable {

    /**
     * Promises a rendering method that draws this object on the given table.
     * @param table the table to render the object on.
     */
    void render(CustomRoomTable table);

    /**
     * Promises the capability to add visible points to the renderable object.
     * @param points the points to be rendered in all future updates.
     */
    void addShownPoints(List<Point> points);

    /**
     * Returns all points in this Renderable.
     */
    List<Point> getShownPoints();

    /**
     * Promises the capability to set entire object to be rendered at once.
     */
    void reveal();

}
