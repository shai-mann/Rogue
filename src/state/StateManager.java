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
        PLAYER_GUI, // updates display to represent new player stats
        PLAYER, // checks for death and level up
        PLAYER_STATUS, // updates player stats (data only)
        PLAYER_HUNGER, // ticks hunger value
        PLAYER_REGEN, // ticks regeneration counter
        //PLAYER_MANA, // ticks mana regeneration
        MONSTERS, // ticks monster AI
        ITEMS, // checks for item pickup
        CHAT, // advances chat (adding new messages, hiding old ones)
        ANIMATIONS, // ticks active animations
    }

    private final Map<Update, UpdateHook> hooksMap = new HashMap<>();
    private final List<RenderHook> renderHooks = new ArrayList<>();
    // todo: add listeners to trigger when any update happens (like the Game marking itself as not saved)

    public void update(StateUpdate state) {
        state.updates.forEach(u -> hooksMap.getOrDefault(u, () -> {
            System.out.println("[DEBUG] Failed to find hook for " + u.toString());
        }).run());

        renderHooks.forEach(Runnable::run);
    }

    public void addHook(Update update, UpdateHook hook) {
        hooksMap.put(update, hook);
    }

    public void addHook(RenderHook hook) {
        renderHooks.add(hook);
    }

}
