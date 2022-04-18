package entity.livingentity.monster.ai.attack;

import entity.livingentity.monster.Monster;
import entity.livingentity.monster.ai.AbstractAttackAI;

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
        player.getStatus().setDrunk(INTOXICATION_TURNS);
        return outcome;
    }
}
