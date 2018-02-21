import javax.swing.*;
import java.awt.*;

public class Room {

    private JPanel panel;
    private JTable table1;

    public Room() {
        setDefaults();

        GameManager.replaceContentPane(panel);
    }
    public void setDefaults() {
        panel.setBackground(Helper.BACKGROUND_COLOR);
        panel.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        panel.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
        panel.setMinimumSize(Toolkit.getDefaultToolkit().getScreenSize());
        panel.setBorder(BorderFactory.createTitledBorder(""));
    }
}
