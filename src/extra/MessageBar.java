package extra;

import helper.Helper;

import javax.swing.*;
import java.awt.*;

public class MessageBar extends JComponent {

    private JPanel panel;
    private JTextArea message;
    private JTextArea oldMessage;
    private JTextArea olderMessage;

    private static MessageBar messageBar;

    public MessageBar() {
        super();
        messageBar = this;
        setDefaults();
    }
    public void setDefaults() {
        panel.setBackground(Helper.BACKGROUND_COLOR);
        panel.setForeground(Helper.BACKGROUND_COLOR);
        setTextareaDefaults(message, Helper.FOREGROUND_COLOR);
        setTextareaDefaults(oldMessage, Helper.changeOpacity(Helper.FOREGROUND_COLOR, 99));
        setTextareaDefaults(olderMessage, Helper.changeOpacity(Helper.FOREGROUND_COLOR, 80));
    }
    private void setTextareaDefaults(JTextArea textArea, Color foreground) {
        textArea.setBackground(Helper.BACKGROUND_COLOR);
        textArea.setForeground(foreground);
        textArea.setFont(new Font(Helper.THEME_FONT, Font.BOLD, 12));
        textArea.setFocusable(false);
    }
    private void _addMessage(String text) {
        String currentMessage = message.getText();
        if (!currentMessage.trim().equals("")) {
            message.setText(currentMessage + "; " + text);
        } else {
            message.setText(text);
        }
    }
    private void _nextTurn() {
        String currentMessage = message.getText();
        String currentOldMessage = oldMessage.getText();

        message.setText("");
        oldMessage.setText(currentMessage);
        olderMessage.setText(currentOldMessage);

        panel.revalidate();
        panel.repaint();
    }

    public static void addMessage(String text) { messageBar._addMessage(text); }
    public static void nextTurn() {
        messageBar._nextTurn();
    }

}
