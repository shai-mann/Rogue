package rendering;

import map.level.table.GameTable;

import java.awt.*;
import java.util.List;

public interface Renderer {

    /**
     * Promises a rendering method that draws this object on the given table.
     * @param table the table to render the object on.
     */
    void render(GameTable table);

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

    /**
     * Returns true if the rendered object is marked as shown, false otherwise.
     */
    boolean shown();

}
