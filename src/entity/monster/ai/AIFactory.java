package entity.monster.ai;

import entity.monster.Monster;
import entity.monster.ai.movement.*;
import entity.monster.ai.attack.*;

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

    private static final Map<String, Function<Monster, AttackAI>> attackAIMap = new HashMap<>() {{
        put("confuse", ConfuseAttack::new);
        put("hp_drain", HealthDrainAttack::new);
        put("hit", HitAttack::new);
        put("intoxicate", IntoxicateAttack::new);
        put("paralyze", ParalyzeAttack::new);
        put("trap", ParalyzeAttack::new); // TODO: trap attack?
        put("rust", RustAttack::new);
        put("steal_gold", StealGoldAttack::new);
        put("steal_item", StealItemAttack::new);
        put("weaken", WeakenAttack::new);
        put("xp_drain", XPDrainAttack::new);
    }};

    public static MovementAI constructMovementAI(String type, Monster monster) {
        return movementAIMap.get(type.toLowerCase()).apply(monster);
    }

    public static AttackAI constructAttackAI(String type, Monster monster) {
        return attackAIMap.get(type.toLowerCase()).apply(monster);
    }

}
