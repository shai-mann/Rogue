package map;

import javax.swing.*;

public class CustomRoomTable extends JTable {

    private RoomTableModel model;

    public CustomRoomTable(RoomTableModel roomTableModel) {
        super(roomTableModel);
        model = roomTableModel;
    }
    public RoomTableModel getCustomModel() {
        return model;
    }
}
