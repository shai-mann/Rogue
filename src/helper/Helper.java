package helper;

import java.awt.*;

public class Helper {

    public static Color BACKGROUND_COLOR = Color.BLACK;
    public static Color FOREGROUND_COLOR = Color.WHITE;

    public static String THEME_FONT = Font.MONOSPACED;

    public static Dimension getScreenSize() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        return toolkit.getScreenSize();
    }
}
