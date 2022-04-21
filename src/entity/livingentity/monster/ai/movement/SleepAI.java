package entity.livingentity.monster.ai.movement;

import entity.Effect;
import entity.livingentity.monster.Monster;
import entity.livingentity.monster.ai.AbstractMovementAI;
import main.GameManager;

public class SleepAI extends AbstractMovementAI {

    public SleepAI(Monster self) {
        super(self);

        // set monster to sleep initially
        self.getStatus().setSleeping(true);
    }

    @Override
    public boolean move() {
        boolean out = super.move();

        if (!self.getStatus().isSleeping() || GameManager.getPlayer().getStatus().hasEffect(Effect.AGGRAVATE_MONSTER)) {
            shouldTriggerSecondaryMovementAI = true; // TODO: make sure when switching to secondary movement, a successful move won't let an attack trigger
        }

        return out;
    }
}
