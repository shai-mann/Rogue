package room;

import javax.swing.table.DefaultTableModel;

public class RoomTableModel extends DefaultTableModel {
    public RoomTableModel() {
        super();
    }
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    @Override
    public void setValueAt(Object o, int x, int y) {
        super.setValueAt(o, x, y);
        fireTableCellUpdated(x, y);
    }
}
