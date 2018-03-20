package extra;

import helper.Helper;
import main.GameManager;

import javax.swing.*;

public class GravePane extends JComponent {

    private JPanel panel;
    private JTextArea textArea;

    public GravePane() {
        GameManager.replaceContentPane(panel);

        setDefaults();
    }
    private void setDefaults() {
        textArea.setEditable(false);
        Helper.setSize(panel, Helper.getScreenSize());
        textArea.setBackground(Helper.BACKGROUND_COLOR);
        textArea.setForeground(Helper.FOREGROUND_COLOR);
        textArea.setText("Your Ad Here \n< Death Message>");

        panel.setBackground(Helper.BACKGROUND_COLOR);
    }
}
