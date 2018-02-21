import java.awt.*;

public class Helper {

    static Color BACKGROUND_COLOR = Color.BLACK;
    static Color FOREGROUND_COLOR = Color.WHITE;

    public static Dimension getScreenSize() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        return toolkit.getScreenSize();
    }
}
