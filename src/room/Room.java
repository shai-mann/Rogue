package room;
import helper.Helper;
import main.GameManager;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;

public class Room {

    /*
    * ROOM CLASS:
    * The room class is made of a grid (represented by a JTable) which things such as the player and monsters
    * can be added to
     */

    private JPanel panel;
    private JTable table;

    private int xLength;
    private int yLength;

    public Room(int x, int y) {
        xLength = x;
        yLength = y;

        setDefaults();

        GameManager.replaceContentPane(panel);
    }
    public void setDefaults() {
        panel.setBackground(Helper.BACKGROUND_COLOR);
        panel.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        panel.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
        panel.setMinimumSize(Toolkit.getDefaultToolkit().getScreenSize());
        panel.setBorder(BorderFactory.createTitledBorder(
                null,
                "Room of Dimensions (x, y)",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                null, Helper.FOREGROUND_COLOR
        ));

        customizeTable();
    }
    public void add(String s, int y, int x) {
        // adds component to table at x and y coords given
        table.getModel().setValueAt(s, y, x);
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here

        RoomTableModel model = createTableModel();
        table = new JTable(model);

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
    public String[] getDefaultRow(String edges, String rests) {
        String[] rowValueList = new String[xLength];
        Arrays.fill(rowValueList, rests);
        rowValueList[0] = edges;
        rowValueList[xLength - 1] = edges;
        return rowValueList;
    }
    public RoomTableModel createTableModel() {
        RoomTableModel model = new RoomTableModel();
        for (int x = 0; x < xLength; x++) {
            model.addColumn("col" + x);
        }
        String[] rowValueList = getDefaultRow("|", "-");
        model.addRow(getDefaultRow("=", "="));
        for (int y = 0; y < yLength - 2; y++) {
            model.addRow(rowValueList);
        }
        model.addRow(getDefaultRow("=", "="));
        return model;
    }
    public void customizeTable() {
        table.setBackground(Helper.BACKGROUND_COLOR);
        table.setGridColor(Helper.BACKGROUND_COLOR);
    }

    public JTable getTable() {
        return table;
    }
}
