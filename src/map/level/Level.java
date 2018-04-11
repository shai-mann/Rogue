package map.level;
import helper.Helper;
import main.GameManager;
import map.CustomCellRenderer;
import map.RoomTableModel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Level extends JComponent {

    /*
    * ROOM CLASS:
    * The map class is made of a grid (represented by a JTable) which things such as the player and monsters
    * can be added to
    * Rooms can only be from 15 length to 5
     */

    private JPanel panel;
    private JTable table;

    private int xLength;
    private int yLength;

    public Level(int x, int y) {
        xLength = x;
        yLength = y;

        setDefaults();
    }
    public void setDefaults() {
        panel.setBackground(Helper.BACKGROUND_COLOR);
        panel.setPreferredSize(new Dimension(GameManager.getFrame().getWidth(), (int) (GameManager.getFrame().getHeight() * 0.8)));
        panel.setMaximumSize(new Dimension(GameManager.getFrame().getWidth(), (int) (GameManager.getFrame().getHeight() * 0.8)));
        panel.setMinimumSize(new Dimension(GameManager.getFrame().getWidth(), (int) (GameManager.getFrame().getHeight() * 0.8)));
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
    public JTable getTable() {
        return table;
    }
}
