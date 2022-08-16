package entity.monster.ai.attack;

import entity.component.Status;
import entity.monster.Monster;
import entity.monster.ai.AbstractAttackAI;
import map.Game;
import util.Helper;

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
            Game.stateModel().message("You are paralyzed for 3 turns"); // TODO: move to Status Listener object?
        }

        player.changeHealth(damage);

        return outcome;
    }
}
