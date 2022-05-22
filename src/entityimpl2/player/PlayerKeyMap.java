package entityimpl2.player;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class PlayerKeyMap {

    // todo: Integer -> Pair<Integer, String>, so that key value maps to String name maps to Action
    private static final Map<Integer, Action> keyMap = new HashMap<>();

    public void addKeyListener(int key, ActionListener listener) {
        keyMap.put(key, new PlayerAction(listener));
    }

    public void apply(JPanel panel) {
        InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = panel.getActionMap();

        for (Integer key : keyMap.keySet()) {
            inputMap.put(KeyStroke.getKeyStroke(key, 0, false), key);
            actionMap.put(key, keyMap.get(key));
        }
    }
}
