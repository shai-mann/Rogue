package entityimpl2.monster.ai.attack;

import entityimpl2.monster.Monster;
import entityimpl2.monster.ai.AbstractAttackAI;

public class XPDrainAttack extends AbstractAttackAI {

    public XPDrainAttack(Monster self) {
        super(self);
    }

    @Override
    protected String successMessage() {
        return "The " + self.name() + " preys on your mind. Your experience drains away";
    }

    @Override
    protected Outcome performAttack(Outcome outcome) {
        player.changeExperience(-(player.getExperience() / 10));
        return outcome;
    }

}
