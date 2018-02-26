import com.sun.istack.internal.Nullable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

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
    }
    public void add(JComponent c, int x, int y) {
        //adds component to table at x and y coords given
    }
    private void createUIComponents() {
        // TODO: place custom component creation code here
        //the number of rows is the x value, y is columns

        table = new JTable();
    }
}
