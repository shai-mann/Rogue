package entity.monster.ai.attack;

import entity.component.Status;
import entity.monster.Monster;
import entity.monster.ai.AbstractAttackAI;

public class IntoxicateAttack extends AbstractAttackAI {

    private static final int INTOXICATION_TURNS = 3;

    public IntoxicateAttack(Monster self) {
        super(self);
    }

    @Override
    protected String successMessage() {
        return "You feel strange";
    }

    @Override
    protected Outcome performAttack(Outcome outcome) {
        player.getStatus().add(Status.Stat.DRUNK, INTOXICATION_TURNS);
        return outcome;
    }
}
