package util;

import entity.Entity;

import javax.swing.*;
import java.awt.*;
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
    }

    public static Color changeOpacity(Color color, int opacity) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
    }

    /* RANDOMNESS METHODS */

    public static int getRandom(int min, int max) {
        return random.nextInt(max - min + 1) + min;
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

    /**
     * Determines if the e1 is in range of e2.
     * @param e1 {@link Entity} searching for second entity.
     * @param e2 {@link Entity} being searched for.
     * @param range range (circular, inclusive) to search in.
     */
    public static boolean isInRange(Entity e1, Entity e2, int range) {
        double dist = Math.hypot(e2.getXPos() - e1.getXPos(), e2.getYPos() - e1.getYPos());

        return dist <= range;
    }
}