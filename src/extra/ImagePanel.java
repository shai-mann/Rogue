package extra;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    public ImagePanel() {

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Toolkit.getDefaultToolkit().createImage("/data/images/Rogue_Background.PNG"), 0, 0, null);
        revalidate();
        repaint();
    }
}
