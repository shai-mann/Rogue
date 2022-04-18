package entity.livingentity.monster.ai.attack;

import entity.livingentity.monster.Monster;
import entity.livingentity.monster.ai.AbstractAttackAI;
import util.Helper;
import util.gamepanes.MessageBar;

public class HitAttack extends AbstractAttackAI {

    private int damage = -1;

    public HitAttack(Monster self) {
        super(self);
    }

    private String getFormattedDamage() {
        if (damage == -1) new Exception("Damage value retrieved before calculation.").printStackTrace();

        return damage == 0 ? "no" : String.valueOf(damage);
    }

    @Override
    protected String successMessage() {
        return "The " + self.getName() + " deals " + getFormattedDamage() + " damage";
    }

    @Override
    protected String critMessage() {
        return "The " + self.getName() + " crits and deals " + getFormattedDamage() + " damage";
    }

    @Override
    protected Outcome performAttack(Outcome outcome) {
        damage = isCrit(outcome) ? self.attributes().critDamage() : self.attributes().hitDamage();

        if (isCrit(outcome) && Helper.calculateChance(0.2)) {
            player.getStatus().setParalyzed(3);
            MessageBar.addMessage("You are paralyzed for 3 turns"); // TODO: move to Status Listener object?
        }

        player.health -= damage; // TODO: make method in player to handle this that can have Listeners

        damage = -1;

        return outcome;
    }
}
