package entityimpl2.monster.ai.attack;

import entityimpl2.component.Status;
import entityimpl2.monster.Monster;
import entityimpl2.monster.ai.AbstractAttackAI;
import util.Helper;
import util.gamepanes.MessageBar;

public class HitAttack extends AbstractAttackAI {

    private int damage = -1;

    public HitAttack(Monster self) {
        super(self);
    }

    private String getFormattedDamage() {
        if (damage == -1) new Exception("Damage value retrieved before calculation.").printStackTrace();

        String out = damage == 0 ? "no" : String.valueOf(damage);

        damage = -1;
        return out;
    }

    @Override
    protected String successMessage() {
        return "The " + self.name() + " deals " + getFormattedDamage() + " damage";
    }

    @Override
    protected String critMessage() {
        return "The " + self.name() + " crits and deals " + getFormattedDamage() + " damage";
    }

    @Override
    protected Outcome performAttack(Outcome outcome) {
        damage = isCrit(outcome) ? self.attributes().critDamage() : self.attributes().hitDamage();

        if (isCrit(outcome) && Helper.calculateChance(0.2)) {
            player.getStatus().add(Status.Stat.PARALYZED, 3);
            MessageBar.addMessage("You are paralyzed for 3 turns"); // TODO: move to Status Listener object?
        }

        player.changeHealth(damage);

        return outcome;
    }
}
