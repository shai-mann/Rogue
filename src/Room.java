import com.sun.istack.internal.Nullable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
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
        createUIComponents();
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

        table.setBackground(Helper.BACKGROUND_COLOR);
    }
    public void add(JComponent c, int x, int y) {
        //adds component to table at x and y coords given
        table.getModel().setValueAt(c, x, y);
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here

        DefaultTableModel model = createTableModel();
        table = new JTable(model);
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
        model.addRow(getDefaultRow("_", "_"));
        for (int y = 0; y < yLength - 2; y++) {
            model.addRow(rowValueList);
        }
        model.addRow(getDefaultRow("_", "_"));
        return model;
    }
}
