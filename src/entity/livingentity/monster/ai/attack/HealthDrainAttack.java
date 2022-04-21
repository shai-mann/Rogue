package entity.livingentity.monster.ai.attack;

import entity.livingentity.monster.Monster;
import entity.livingentity.monster.ai.AbstractAttackAI;

public class HealthDrainAttack extends AbstractAttackAI {

    public HealthDrainAttack(Monster self) {
        super(self);
    }

    @Override
    protected String successMessage() {
        return "The " + self.getName() + " preys on your flesh. Your health capacity drains away";
    }

    @Override
    protected Outcome performAttack(Outcome outcome) {
        player.drainMaxHealth((int) -(player.getMaxHealth() * 0.9));

        return outcome;
    }

}
