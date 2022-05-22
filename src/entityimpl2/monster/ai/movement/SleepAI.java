package entityimpl2.monster.ai.movement;

import entityimpl2.component.Effect;
import entityimpl2.component.Status;
import entityimpl2.monster.Monster;
import entityimpl2.monster.ai.AbstractMovementAI;
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
