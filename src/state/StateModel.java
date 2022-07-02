package state;

import entity.player.Player;
import util.animation.AnimationManager;
import util.gamepanes.MessageBar;

import java.util.function.Supplier;

/**
 * Maintains hooks to various important aspects of the game state that other parts of the model
 * frequently use. These include: the {@link entity.player.Player}, the {@link util.animation.AnimationManager},
 * the {@link StateManager}, and the Debug Logger (todo).
 */
public class StateModel {

    private final Supplier<Player> player;
    private final Supplier<AnimationManager> animationManager;
    private final StateManager stateManager;

    public StateModel(Supplier<Player> player, Supplier<AnimationManager> animationManager) {
        this.player = player;
        this.animationManager = animationManager;
        this.stateManager = new StateManager(this);
    }

    public Player getPlayer() {
        return player.get();
    }

    public AnimationManager getAnimationManager() {
        return animationManager.get();
    }

    public void update(StateUpdate stateUpdate) {
        this.stateManager.update(stateUpdate);
    }

    public void message(String message) {
        MessageBar.addMessage(message);
    }

    public void debug(String message) {
        System.out.println("[DEBUG] " + message);
    }

    public StateManager getStateManager() {
        return stateManager;
    }
}
