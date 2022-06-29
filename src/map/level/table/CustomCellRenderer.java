package map.level.table;
import util.Helper;
import map.Game;
import util.animation.Animation;

import javax.swing.table.DefaultTableCellRenderer;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class CustomCellRenderer extends DefaultTableCellRenderer {

    // todo: convert Point -> Color to Point -> custom class X:
    // X has Color and int (z value), when new colors are given to color map, higher z value is kept
    // this leaves the problem of if a color is removed from the color map (an animation dies out, monster moves)
    // so either the map has to be reset a lot, or some other form of data structure should be used.
    // the alternative is to use encoded z values to decide what objects render at what times,
    // so that all objects are applying colors to the map (and then clearing it) properly
    public static java.util.Map<Point, Color> colorMap = new HashMap<>();

    public Component getTableCellRendererComponent (
            JTable table, Object obj, boolean isSelected, boolean hasFocus, int row, int column
        ) {
        Component cell = super.getTableCellRendererComponent(
                table, obj, isSelected, hasFocus, row, column);
        cell.setBackground(Helper.BACKGROUND_COLOR);
        cell.setForeground(getForegroundColor(new Point(column, row)));

        return cell;
    }

    private Color containsPoint(Point p) {
        Animation a = Game.getMap().getAnimationManager().contains(p);
        if (a != null) {
            return a.getColor();
        }
        return null;
    }

    private Color getForegroundColor(Point p) {
        return colorMap.getOrDefault(p, Helper.FOREGROUND_COLOR);
    }
}
