package map.level.table;
import helper.Helper;
import javax.swing.table.DefaultTableCellRenderer;

import javax.swing.*;
import java.awt.*;

public class CustomCellRenderer extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent (
            JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column
        ) {
        Component cell = super.getTableCellRendererComponent(
                table, obj, isSelected, hasFocus, row, column);
        cell.setBackground(Helper.BACKGROUND_COLOR);
        cell.setForeground(getForegroundColor((String) table.getModel().getValueAt(row, column)));

        return cell;
    }
    private Color getForegroundColor(String value) {
        if (value == "@") {
            return Color.YELLOW;
        } else if (value.matches("[a-zA-Z]")) { // monsters
            return Color.RED;
        } else if (value.equals("*") || value.equals("]") || value.equals("&") || value.equals("%") ||
                value.equals("?") || value.equals("/") || value.equals("^") || value.equals(":") ||
                value.equals("!") || value.equals(")")) {
            return Color.GREEN;
        } else {
            return Helper.FOREGROUND_COLOR;
        }
    }
}
