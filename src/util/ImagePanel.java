package util;

import main.GameManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ImagePanel extends JPanel {

    private String filePath;
    private Container panel;

    public ImagePanel(String path, Container frame) {
        super();
        filePath = path;
        panel = frame;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            Image image = ImageIO.read(ImagePanel.class.getClassLoader().getResourceAsStream(filePath));
            g.drawImage(image, (panel.getWidth() / 2) - (image.getWidth(null) / 2),
                    (panel.getHeight() / 2) - (image.getHeight(null) / 2), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
