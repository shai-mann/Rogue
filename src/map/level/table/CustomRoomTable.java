package map.level.table;

import com.sun.istack.internal.Nullable;

import javax.swing.*;
import java.io.Serializable;

public class CustomRoomTable extends JTable implements Serializable {

    private RoomTableModel model;
    private boolean tableType = false;

    public CustomRoomTable(RoomTableModel roomTableModel, boolean tt) {
        super(roomTableModel);
        tableType = tt;
        model = roomTableModel;
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
