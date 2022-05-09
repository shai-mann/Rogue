package util.gamepanes;

import util.Helper;
import main.GameManager;

import javax.swing.*;
import java.awt.*;

public class GravePane extends JComponent {

    private JPanel panel;
    private JTextArea textArea;

    public GravePane() {
        if (GameManager.getFrame().getWindowListeners().length > 0) {
            GameManager.getFrame().removeWindowListener(GameManager.getFrame().getWindowListeners()[0]);
        }
        GameManager.getFrame().setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        GameManager.replaceContentPane(panel);

        setDefaults();
        GameManager.getFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void setDefaults() {
        textArea.setEditable(false);
        textArea.setBackground(Helper.BACKGROUND_COLOR);
        textArea.setForeground(Helper.FOREGROUND_COLOR);
        textArea.setFont(new Font(Helper.THEME_FONT, Font.BOLD, 30));
        addLine("     .-------.");
        addLine("   .'         '.");
        addLine("   |  R  I  P  |");
        addLine("   |           |");
        addLine("   |   score   |");
        addLine(scoreLine());
        addLine("   |           |");
        addLine(" ^^^^^^^^^^^^^^^^^");

        panel.setBackground(Helper.BACKGROUND_COLOR);
    }

    private void addLine(String text) {
        textArea.setText(textArea.getText() + "\n" + text);
    }

    private String scoreLine() {
        String string = "   |";
        for (int i = 0; i < (6 - GameManager.getPlayer().getExperienceDigitsNumber() / 2) - 1; i++) {
            string = string.concat(" ");
        }
        string = string.concat(String.valueOf(GameManager.getPlayer().getExperience()));
        for (int i = 0; i < (6 - GameManager.getPlayer().getExperienceDigitsNumber() / 2) - 1; i++) {
            string = string.concat(" ");
        }
        string = string.concat("|");
        return string;
    }

}
