import javax.swing.table.DefaultTableModel;

public class RoomTableModel extends DefaultTableModel {
    public RoomTableModel() {
        super();
    }
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
