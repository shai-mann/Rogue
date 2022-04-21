package entity.livingentity.monster.ai.attack;

import entity.livingentity.monster.Monster;
import entity.livingentity.monster.ai.AbstractAttackAI;
import util.Helper;

public class ConfuseAttack extends AbstractAttackAI {

    private static final int CONFUSED_TURNS_MIN = 10, CONFUSED_TURN_MAX = 21;

    public ConfuseAttack(Monster self) {
        super(self);
    }

    @Override
    protected String successMessage() {
        return "You feel confused";
    }

    @Override
    protected Outcome performAttack(Outcome outcome) {
        player.getStatus().setConfused(Helper.getRandom(CONFUSED_TURNS_MIN, CONFUSED_TURN_MAX));
        return outcome;
    }
}
