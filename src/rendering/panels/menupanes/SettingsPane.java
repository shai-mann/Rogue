package rendering.panels.menupanes;

import javafx.util.Pair;
import main.GameManager;
import map.level.table.CustomRoomTable;
import map.level.table.RoomTableModel;
import settings.Settings;
import util.Helper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class SettingsPane {

    private JPanel innerPanel;
    private JComboBox textSizeSelector;
    private JLabel textSizeField;
    private JButton changeButton;
    private JButton cancelButton;
    private JPanel panel;
    private JScrollPane scrollPane;
    private JLabel keyLabel;
    private CustomRoomTable keyValuesTable;

    private final JPanel savedPane;

    private static ArrayList<Pair> actionQueue = new ArrayList<>();

    public SettingsPane(JPanel oldContentPane) {
        savedPane = oldContentPane;
        GameManager.replaceContentPane(panel);

        setDefaults();
    }

    private void setDefaults() {
        setDefaultSettings(innerPanel);
        setDefaultSettings(textSizeField);
        setDefaultSettings(textSizeSelector);
        setDefaultSettings(changeButton);
        setDefaultSettings(cancelButton);
        setDefaultSettings(panel);
        setDefaultSettings(scrollPane);
        setDefaultSettings(changeButton);
        setDefaultSettings(keyLabel);
        setDefaultSettings(keyValuesTable);

        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, Integer.MAX_VALUE));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.setSize(new Dimension(panel.getPreferredSize().width + 150, panel.getPreferredSize().height));

        textSizeSelector.setEditable(false);

        textSizeSelector.addItem("Small"); // 12 pt
        textSizeSelector.addItem("Normal"); // 24 pt
        textSizeSelector.addItem("Large"); // 36 pt
        textSizeSelector.setSelectedIndex(Settings.getTextSize() / 12 - 1);

        keyValuesTable.setFocusable(true);
        keyValuesTable.setRowSelectionAllowed(false);
        keyValuesTable.setFont(new Font(Helper.THEME_FONT, Font.BOLD, 15));
        keyValuesTable.setRowHeight(30);
        keyValuesTable.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        keyValuesTable.setGridColor(Color.WHITE);

        addActionListeners();
    }

    private void setDefaultSettings(JComponent jc) {
        jc.setBackground(Helper.BACKGROUND_COLOR);
        jc.setForeground(Helper.FOREGROUND_COLOR);
        jc.setFont(Font.getFont(Helper.THEME_FONT));
    }

    private void addActionListeners() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionQueue.clear();
                GameManager.replaceContentPane(savedPane);
            }
        });
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Settings.setTextSize(textSizeSelector.getSelectedIndex() + 1);
                for (int i = 0; i < actionQueue.size(); i++) {
                    Pair p = actionQueue.get(i);
                    Settings.setKeyBind((Integer) p.getKey(), (Integer) p.getValue());
                }
                GameManager.replaceContentPane(savedPane);
            }
        });
        keyValuesTable.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                int x = keyValuesTable.getSelectedColumn();
                int y = keyValuesTable.getSelectedRow();
                if (x == 1 && Settings.checkValidKey(e.getKeyCode())) {
                    actionQueue.add(new Pair<>(y - 1, e.getKeyCode()));
                    keyValuesTable.setValueAt(Character.toUpperCase(e.getKeyChar()), y, x);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    public static ArrayList<Pair> getActionQueue() {
        return actionQueue;
    }

    private void createUIComponents() {
        RoomTableModel model = createTableModel();
        keyValuesTable = new CustomRoomTable(model, true);
    }

    private RoomTableModel createTableModel() {
        RoomTableModel model = new RoomTableModel();
        model.addColumn("Action:");
        model.addColumn("Key:");
        model.addRow(new String[]{"Action:", "Key:"});
        for (int i = 0; i < 8; i++) {
            model.addRow(new String[]{Settings.keyCodeToString(Settings.getKeys().get(i)),
                    KeyEvent.getKeyText(Settings.getKeys().get(i))});
        }
        return model;
    }

}
