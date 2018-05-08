package extra;

import main.GameManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel {

    public ImagePanel() {
        super();
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        try {
            Image image = ImageIO.read(new File("data/images/Rogue_Background.PNG"));
            g.drawImage(image, (GameManager.getFrame().getWidth() / 2) - (image.getWidth(null) / 2),
                    (GameManager.getFrame().getHeight() / 2) - (image.getHeight(null) / 2), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
