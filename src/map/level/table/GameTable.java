package map.level.table;

import util.Helper;

import javax.swing.*;
import java.awt.*;

public class GameTable {

    private static final int NUM_ROWS = 40, NUM_COLS = 69;

    private JTable table;
    private JPanel panel;

    public void add(String s, Point p) {
        this.add(s, p.x, p.y);
    }

    public void add(String s, int x, int y) {
        // reverse x and y because rows, columns is y, x
        table.setValueAt(s, y, x);
    }

    public void setColor(Point p, Color c) {
        CustomCellRenderer.colorMap.put(p, c);
    }

    public void clear() {
        for (int x = 0; x < table.getModel().getColumnCount(); x++) {
            for (int y = 0; y < table.getModel().getRowCount(); y++) {
                add("", x, y);
            }
        }
    }

    private void createUIComponents() {
        table = new JTable(new RoomTableModel(NUM_ROWS, NUM_COLS));

        table.setBackground(Helper.BACKGROUND_COLOR);
        table.setForeground(Helper.BACKGROUND_COLOR);
        table.setGridColor(Helper.BACKGROUND_COLOR);
        table.setFont(new Font(Helper.THEME_FONT, Font.BOLD, 12));

        table.setDefaultRenderer(String.class, new CustomCellRenderer());
    }

    public JPanel getPanel() {
        return panel;
    }

    public JTable getTable() {
        return table;
    }

    public static boolean isOnTable(Point p) {
        return Helper.isInRange(p.x, 0, NUM_COLS) && Helper.isInRange(p.y, 0, NUM_ROWS);
    }

    public static boolean isOnTable(Rectangle bounds) {
        return new Rectangle(0, 0, NUM_COLS, NUM_ROWS).contains(bounds);
    }

    /**
     * Determines if the given {@link Point} is within the boundaries of the GameTable and
     * not directly against the edge of the table
     * @param p
     * @return
     */
    public static boolean isNotAgainstEdge(Point p) {
        return Helper.isInRange(p.x, 1, NUM_COLS - 1) && Helper.isInRange(p.y, 1, NUM_ROWS - 1);
    }
}
