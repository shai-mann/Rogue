package entity.livingentity.monster.ai.attack;

import entity.livingentity.monster.Monster;
import entity.livingentity.monster.ai.AbstractAttackAI;

public class ParalyzeAttack extends AbstractAttackAI {

    private static final int PARALYSIS_TURNS = 3;

    public ParalyzeAttack(Monster self) {
        super(self);
    }

    @Override
    protected String successMessage() {
        return "You've been paralyzed";
    }

    @Override
    protected Outcome performAttack(Outcome outcome) {
        if (!player.getStatus().isParalyzed()) {
            player.getStatus().setParalyzed(PARALYSIS_TURNS);
        }

        return outcome;
    }
}
