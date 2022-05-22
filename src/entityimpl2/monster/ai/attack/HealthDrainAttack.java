package entityimpl2.monster.ai.attack;


import entityimpl2.monster.Monster;
import entityimpl2.monster.ai.AbstractAttackAI;

public class HealthDrainAttack extends AbstractAttackAI {

    public HealthDrainAttack(Monster self) {
        super(self);
    }

    @Override
    protected String successMessage() {
        return "The " + self.name() + " preys on your flesh. Your health capacity drains away";
    }

    @Override
    protected Outcome performAttack(Outcome outcome) {
        player.changeMaxHealth(-(player.maxHealth() / 10));

        return outcome;
    }

}
