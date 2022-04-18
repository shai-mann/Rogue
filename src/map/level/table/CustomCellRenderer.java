package map.level.table;
import util.Helper;
import map.Map;
import util.animation.Animation;

import javax.swing.table.DefaultTableCellRenderer;

import javax.swing.*;
import java.awt.*;

public class CustomCellRenderer extends DefaultTableCellRenderer {

    int row;
    int col;

    public Component getTableCellRendererComponent (
            JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column
        ) {
        this.row = row;
        this.col = column;
        Component cell = super.getTableCellRendererComponent(
                table, obj, isSelected, hasFocus, row, column);
        cell.setBackground(Helper.BACKGROUND_COLOR);
        cell.setForeground(getForegroundColor((String) table.getModel().getValueAt(row, column)));

        return cell;
    }
    private Color containsPoint(Point p) {
        Animation a = Map.getMap().getAnimationManager().contains(p);
        if (a != null) {
            return a.getColor();
        }
        return null;
    }
    private Color getForegroundColor(String value) {
        if (value.equals("@")) {
            return Color.YELLOW;
        } else if (containsPoint(new Point(col, row)) != null) {
            return containsPoint(new Point(col, row));
        } else if (value.matches("[a-zA-Z]")) { // data.monsters
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
