package util;

import main.GameManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ImagePanel extends JPanel {

    public ImagePanel() {
        super();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            Image image = ImageIO.read(ImagePanel.class.getClassLoader().getResourceAsStream("data/images/Rogue_Background.PNG"));
            g.drawImage(image, (GameManager.getFrame().getWidth() / 2) - (image.getWidth(null) / 2),
                    (GameManager.getFrame().getHeight() / 2) - (image.getHeight(null) / 2), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
