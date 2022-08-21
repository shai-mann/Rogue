package rendering.panels.gamepanes;

import map.Game;
import settings.Settings;
import util.Helper;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class Chat extends JComponent {
    private JPanel panel;
    private JScrollPane scrollPane;
    private JComboBox<String> chat;

    public Chat() {
        setDefaults();
    }

    public void addMessage(String message) {
        try {
            document.insertString(document.getLength(), message, style);
        } catch (BadLocationException e) {
            Game.stateModel().debug("Bad location exception. Suppressing the message: '" + message + "'");
        }
    }

    private void setDefaults() {
        panel.setBackground(Helper.BACKGROUND_COLOR);
        panel.setForeground(Helper.BACKGROUND_COLOR);
        panel.setLocation(0, 0);
        panel.setSize(panel.getPreferredSize());
        scrollPane.setBackground(Helper.BACKGROUND_COLOR);
        scrollPane.setForeground(Helper.BACKGROUND_COLOR);
        chat.setBackground(Helper.BACKGROUND_COLOR);
        chat.setForeground(Helper.FOREGROUND_COLOR);
        chat.setFont(new Font(Helper.THEME_FONT, Font.BOLD, Settings.getTextSize()));
        chat.setFocusable(false);
    }
}
