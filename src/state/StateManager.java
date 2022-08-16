package state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The StateManager manages updating and rendering the game. It is given {@link StateUpdate} objects
 * on update that indicate which components to update before rendering.
 */
public class StateManager {

    // todo: force ordering using enum field + sorting of list
    public enum Update {
        CHAT(0), // advances chat (adding new messages, hiding old ones)
        PLAYER(1), // checks for death and level up
        PLAYER_STATUS(2), // updates player stats (data only)
        PLAYER_HUNGER(3), // ticks hunger value
        PLAYER_REGEN(4), // ticks regeneration counter
        //PLAYER_MANA, // ticks mana regeneration
        MONSTERS(5), // ticks monster AI
        PLAYER_GUI(6), // updates display to represent new player stats
        ITEMS(7), // checks for item pickup
        ANIMATIONS(8); // ticks active animations

        final int z;

        Update(int z) {
            this.z = z;
        }

        public int compare(Update that) {
            return Integer.compare(this.z, that.z);
        }
    }

    private final Map<Update, UpdateHook> hooksMap = new HashMap<>();
    private final List<UpdateListener> listeners = new ArrayList<>();

    private final StateModel model;

    public StateManager(StateModel model) {
        this.model = model;
    }

    public void update(StateUpdate state) {
        List<Update> updates = state.updates.stream().sorted(Update::compare).toList();
        updates.forEach(u -> hooksMap.getOrDefault(u, () -> {
            model.debug("Failed to find hook for " + u.toString());
        }).run());

        listeners.forEach(Runnable::run);
    }

    public void addHook(Update update, UpdateHook hook) {
        hooksMap.put(update, hook);
    }

    public void addListener(UpdateListener listener) {
        listeners.add(listener);
    }

}
