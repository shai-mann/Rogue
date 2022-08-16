package state;

import state.StateManager.Update;

import java.util.*;

/**
 * A StateUpdate represents an update to the game state. Various types of updates can occur,
 * and those can cause various subsets of the components of the game to update/refresh. This class
 * is initialized with values to indicate which subset should update.
 *
 * @see StateManager
 */
public class StateUpdate {

    public static final StateUpdate GAME_TICK = new StateUpdate();

    final Set<Update> updates = new HashSet<>();

    public StateUpdate() {
        this(Arrays.stream(Update.values()).toList());
    }

    public StateUpdate(Update update) {
        this(Collections.singletonList(update));
    }

    public StateUpdate(List<Update> updates) {
        this.updates.addAll(updates);
    }

    public void add(Update update) {
        updates.add(update);
    }

    public void remove(Update update) {
        updates.remove(update);
    }

}
