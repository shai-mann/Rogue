package util;

import helper.Helper;

import javax.swing.*;

public class SettingsPane {

    private JPanel scrollpanel;

    private JPanel savedPane;

    public SettingsPane(JPanel oldContentPane) {
        savedPane = oldContentPane;
    }
    private void setDefaults() {
        scrollpanel.setBackground(Helper.BACKGROUND_COLOR);
        scrollpanel.setForeground(Helper.FOREGROUND_COLOR);
    }
}
