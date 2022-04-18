package entity.livingentity.monster.ai;

import entity.livingentity.monster.Monster;
import entity.livingentity.monster.ai.movement.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A simple factory class for {@link MovementAI} subgroups.
 */
public class AIFactory {

    private static final Map<String, Function<Monster, MovementAI>> movementAIMap = new HashMap<>() {{
        put("mimic", MimicAI::new);
        put("sleep", SleepAI::new);
        put("still", StillAI::new);
        put("track", TrackAI::new);
        put("wander", WanderAI::new);
    }};

    private static final Map<String, Supplier<MovementAI>> unlinkedMovementAIMap = new HashMap<>() {{
        put("mimic", MimicAI::new);
        put("sleep", SleepAI::new);
        put("still", StillAI::new);
        put("track", TrackAI::new);
        put("wander", WanderAI::new);
    }};

    public static MovementAI constructMovementAI(String type) {
        return unlinkedMovementAIMap.get(type.toLowerCase()).get();
    }

    public static MovementAI constructMovementAI(String type, Monster monster) {
        return movementAIMap.get(type.toLowerCase()).apply(monster);
        // TODO: determine if necessary to construct AI with source set initially (are all made without source initially?)
    }

    // TODO: add AttackAI factory methods

}
