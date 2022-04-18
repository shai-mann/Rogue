package entity.livingentity.monster.ai;

import entity.livingentity.monster.Monster;
import entity.livingentity.monster.ai.movement.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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

    public static MovementAI constructMovementAI(String type, Monster monster) {
        return movementAIMap.get(type.toLowerCase()).apply(monster);
    }

    // TODO: add AttackAI factory methods

}
