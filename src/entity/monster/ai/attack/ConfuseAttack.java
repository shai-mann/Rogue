package entity.monster.ai.attack;

import entity.component.Status;
import entity.monster.Monster;
import entity.monster.ai.AbstractAttackAI;
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
        if (!player.getStatus().is(Status.Stat.CONFUSED)) {
            player.getStatus().add(Status.Stat.CONFUSED, Helper.getRandom(CONFUSED_TURNS_MIN, CONFUSED_TURN_MAX));
            return outcome;
        }

        return Outcome.UNPERFORMED;
    }
}
