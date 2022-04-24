package map.level.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.Serializable;

public class CustomRoomTable extends JTable implements Serializable {

    private final RoomTableModel model;
    private final boolean tableType;

    public CustomRoomTable(RoomTableModel roomTableModel, boolean tt) {
        super(roomTableModel);
        tableType = tt;
        model = roomTableModel;
    }

    public void add(String s, Point p) {
        this.add(s, p.x, p.y);
    }

    public void add(String s, int x, int y) {
        // reverse x and y because rows, columns is y, x
        this.setValueAt(s, y, x);
    }

    public void clear() {
        for (int x = 0; x < model.getColumnCount(); x++) {
            for (int y = 0; y < model.getRowCount(); y++) {
                add("", x, y);
            }
        }
    }

    public void setColumnWidth(int width) {
        for (int i = 0; i < getColumnCount(); i++) {
            getColumnModel().getColumn(i).setWidth(width);
        }
    }

    public void setCellRenderer(TableCellRenderer renderer) {
        for (int i = 0; i < getColumnCount(); i++) {
            getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(new Point(0, 0), new Dimension(getColumnCount(), getRowCount()));
    }

    @Override
    public void changeSelection(int row, int col, boolean toggle, boolean extend) {
        if (tableType && col == 1 && row > 0) {
            super.changeSelection(row, col, toggle, extend);
        }
    }
    public RoomTableModel getCustomModel() {
        return model;
    }
}
