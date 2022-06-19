package entity.player;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerAction extends AbstractAction {

    private final ActionListener listener;

    public PlayerAction(ActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        listener.actionPerformed(e);
    }
}
