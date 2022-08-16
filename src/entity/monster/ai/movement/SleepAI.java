package entity.monster.ai.movement;

import entity.component.Effect;
import entity.component.Status;
import entity.monster.Monster;
import entity.monster.ai.AbstractMovementAI;
import map.level.Level;

public class SleepAI extends AbstractMovementAI {

    public SleepAI(Monster self) {
        super(self);

        // set monster to sleep initially
        self.getStatus().add(Status.Stat.SLEEPING, 999999);
    }

    @Override
    public boolean shouldTriggerSecondaryAI() {
        return !self.getStatus().is(Status.Stat.SLEEPING) ||
                Level.getLevel().getPlayer().getStatus().hasEffect(Effect.Type.AGGRAVATE_MONSTER);
    }
}
