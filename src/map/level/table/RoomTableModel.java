package map.level.table;

import javax.swing.table.DefaultTableModel;
import java.util.Collections;

public class RoomTableModel extends DefaultTableModel {

    // todo: remove this constructor and its references
    public RoomTableModel() {
        super();
    }

    public RoomTableModel(int rows, int cols) {
        super(
                Collections.nCopies(rows, Collections.nCopies(cols, "").toArray(new String[0])).toArray(new String[0][0]),
                Collections.nCopies(cols, "").toArray()
        );
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
