package entity.livingentity.monster.ai.movement;

import entity.Effect;
import entity.livingentity.monster.Monster;
import entity.livingentity.monster.ai.AbstractMovementAI;
import main.GameManager;

public class SleepAI extends AbstractMovementAI {

    public SleepAI() {}

    public SleepAI(Monster self) {
        super(self);

        // set monster to sleep initially
        self.getStatus().setSleeping(true);
    }

    @Override
    public void move() {
        super.move();

        if (!self.getStatus().isSleeping() || GameManager.getPlayer().getStatus().hasEffect(Effect.AGGRAVATE_MONSTER)) {
            shouldTriggerSecondaryMovementAI = true;
        }
    }
}