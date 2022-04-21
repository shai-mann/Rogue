package entity.livingentity.monster.ai.attack;

import entity.component.Effect;
import entity.component.Status;
import entity.livingentity.monster.Monster;
import entity.livingentity.monster.ai.AbstractAttackAI;

public class RustAttack extends AbstractAttackAI {

    public RustAttack(Monster self) {
        super(self);
    }

    @Override
    protected String successMessage() {
        return "Your armor feels worse";
    }

    @Override
    protected String blockedMessage() {
        return "The " + self.getName() + "'s attack was magically deflected";
    }

    @Override
    protected Outcome performAttack(Outcome outcome) {
        Status status = player.getStatus();

        if (player.getWornItem() == null) {
            return Outcome.UNPERFORMED;
        } else if (status.hasEffect(Effect.PROTECT_ARMOR)) {
            return Outcome.BLOCKED;
        }

        status.setAc(status.getAc() + 1);
        return outcome;
    }

}
