package state;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The StateManager manages updating and rendering the game. It is given {@link StateUpdate} objects
 * on update that indicate which components to update before rendering.
 */
public class StateManager {

    enum Update {
        PLAYER_GUI,
        PLAYER_STATUS,
        PLAYER_HUNGER,
        PLAYER_REGEN,
        //PLAYER_MANA,
        MONSTERS,
        ITEMS,
        CHAT,
        ANIMATIONS,
    }

    private final Map<Update, Consumer<StateUpdate>> hooksMap = new HashMap<>();

    public void update(StateUpdate state) {
        state.updates.forEach(u -> hooksMap.getOrDefault(u, u1 -> {
            System.out.println("[DEBUG] Failed to find hook for " + u1.toString());
        }).accept(state));
    }

    public void addHook(Update update, Consumer<StateUpdate> consumer) {
        hooksMap.put(update, consumer);
    }

}
