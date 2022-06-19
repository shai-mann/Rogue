package rendering;

import map.level.table.GameTable;

public interface Renderable {

    /**
     * Promises a rendering method that draws this object on the given table.
     * @param table the table to render the object on.
     */
    void render(GameTable table);
}
