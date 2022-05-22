package entity.monster.ai.attack;

import entity.component.Effect;
import entity.component.Status;
import entity.monster.Monster;
import entity.monster.ai.AbstractAttackAI;

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
        return "The " + self.name() + "'s attack was magically deflected";
    }

    @Override
    protected Outcome performAttack(Outcome outcome) {
        Status status = player.getStatus();

        if (player.getWornItem() == null) {
            return Outcome.UNPERFORMED;
        } else if (status.hasEffect(Effect.Type.PROTECT_ARMOR)) {
            return Outcome.BLOCKED;
        }

        status.setAc(status.getAc() + 1);
        return outcome;
    }

}
