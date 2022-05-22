package util;

import entityimpl2.structure.Entity;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Helper {

    public static Color BACKGROUND_COLOR = Color.BLACK;
    public static Color FOREGROUND_COLOR = Color.WHITE;

    public static String THEME_FONT = Font.MONOSPACED;

    public static Random random = new Random();

    public static Dimension getScreenSize() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        return toolkit.getScreenSize();
    }

    public static void setSize(JComponent component, Dimension size) {
        component.setPreferredSize(size);
        component.setMaximumSize(size);
        component.setMinimumSize(size);
        component.setSize(size);
    }

    public static Color changeOpacity(Color color, int opacity) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
    }

    /* RANDOMNESS METHODS */

    public static int getRandom(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * Given a double between 0.0 and 1.0, treats this as a % chance of success (0.2 -> 20%),
     * and determines if the chance succeeds randomly.
     * @param chance the percent chance of success (as decimal).
     * @return true if the chance succeeds, false otherwise
     */
    public static boolean calculateChance(double chance) {
        return random.nextDouble() <= chance;
    }

    public static <T> T getRandom(List<T> objects) {
        return objects.get(random.nextInt(objects.size()));
    }

    public static String createRandomString(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyz";
        String string = "";
        for (int i = 0; i < length; i++) {
            string = string.concat(String.valueOf(chars.charAt(random.nextInt(chars.length() - 1))));
        }
        return string;
    }

    /* ENTITY HELPER METHODS */

    public static double distance(Entity e1, Entity e2) {
        return e1.location().distance(e2.location());
    }

    /**
     * Determines if the e1 is in range of e2.
     * @param e1 {@link Entity} searching for second entity.
     * @param e2 {@link Entity} being searched for.
     * @param range range (circular, inclusive) to search in.
     */
    public static boolean isInRange(Entity e1, Entity e2, int range) {
        double dist = distance(e1, e2);

        return dist <= range;
    }

    public static boolean isNextTo(Entity e1, Entity e2) {
        return isInRange(e1, e2, 1);
    }

    public static boolean isNextTo(Point p1, Point p2) {
        return getAdjacentPoints(p1, true).contains(p2);
    }

    /* RENDERING HELPER METHODS */

    public static List<Point> getAdjacentPoints(Point p, boolean includeGiven) {
        List<Point> points = new ArrayList<>(List.of(new Point[]{
                new Point(p.x, p.y + 1),
                new Point(p.x, p.y - 1),
                new Point(p.x + 1, p.y),
                new Point(p.x - 1, p.y)}));

        if (includeGiven) {
            points.add(p);
        }

        return points;
    }

    /* GENERAL MATH HELPER METHODS */

    /**
     * Determines if the given value is in the range given (inclusive)
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * Translates the given point, but does not mutate either given point.
     * @return a new point where the translated point would land.
     */
    public static Point translate(Point p, Point dp) {
        return new Point(p.x + dp.x, p.y + dp.y);
    }

    public static Dimension translate(Dimension dim1, Dimension dim2) {
        return new Dimension(dim1.width + dim2.width, dim1.height + dim2.height);
    }
}
