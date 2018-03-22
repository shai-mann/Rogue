package map.level;
import helper.Helper;
import main.GameManager;
import map.CustomCellRenderer;
import map.CustomRoomTable;
import map.RoomTableModel;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.Arrays;

public class Level extends JComponent {

    /*
    * ROOM CLASS:
    * The map class is made of a grid (represented by a JTable) which things such as the player and monsters
    * can be added to
    * Rooms can only be from 15 length to 5
    * Rooms must be odd lengths
     */

    private JPanel panel;
    private CustomRoomTable table;
    private Room room;

    public Level(int x, int y) {
        setDefaults();
        createRooms();
    }
    /*
    * Level size is 69 x 39
     */
    public void createRooms() {
        room = new Room(table.getCustomModel(), 5, 5, new Dimension(11, 11));
        new Room(table.getCustomModel(), 30, 30, new Dimension(5, 5));
        new Room(table.getCustomModel(), 60, 10, new Dimension(9, 9));
    }
    public void setDefaults() {
        panel.setBackground(Helper.BACKGROUND_COLOR);
        panel.setPreferredSize(new Dimension(GameManager.getFrame().getWidth(), (int) (GameManager.getFrame().getHeight() * 0.9)));
        panel.setMaximumSize(new Dimension(GameManager.getFrame().getWidth(), (int) (GameManager.getFrame().getHeight() * 0.9)));
        panel.setMinimumSize(new Dimension(GameManager.getFrame().getWidth(), (int) (GameManager.getFrame().getHeight() * 0.9)));
        panel.setBorder(null);
        table.setBackground(Helper.BACKGROUND_COLOR);
        table.setGridColor(Helper.BACKGROUND_COLOR);
    }
    public void add(String s, int x, int y) {
        // adds component to table at x and y coordinates given
        table.getModel().setValueAt(s, y, x);
    }
    public String getValueAt(int x, int y) {
        return (String) table.getValueAt(y, x);
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here

        RoomTableModel model = createTableModel();
        table = new CustomRoomTable(model);

        table.setFocusable(false);
        table.setRowSelectionAllowed(false);
        table.setForeground(Helper.BACKGROUND_COLOR);
        table.setGridColor(Helper.BACKGROUND_COLOR);
        table.setFont(new Font(Helper.THEME_FONT, Font.BOLD, 12));

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new CustomCellRenderer());
            table.getColumnModel().getColumn(i).setMinWidth(20);
            table.getColumnModel().getColumn(i).setMaxWidth(20);
        }
    }
    private RoomTableModel createTableModel() {
        RoomTableModel model = new RoomTableModel();
        for (int j = 0; j != 69; j++) {
            model.addColumn("");
        }
        for (int i = 0; i != 39; i++) {
            model.addRow(createTableRow());
        }
        return model;
    }
    private String[] createTableRow() {
        String[] rowValueList = new String[99];
        Arrays.fill(rowValueList, "");

        return rowValueList;
    }
    public JTable getTable() {
        return table;
    }
}
