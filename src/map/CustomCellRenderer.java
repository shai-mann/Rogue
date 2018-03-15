package map;
import helper.Helper;
import javax.swing.table.DefaultTableCellRenderer;

import javax.swing.*;
import java.awt.*;

public class CustomCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent (
            JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column
        ) {
        Component cell = super.getTableCellRendererComponent(
                table, obj, isSelected, hasFocus, row, column);

        cell.setBackground(Helper.BACKGROUND_COLOR);
        cell.setForeground(Helper.FOREGROUND_COLOR);

        return cell;
    }
}
