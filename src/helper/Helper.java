package helper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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
    public static Object getRandom(ArrayList objects) {
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
}
