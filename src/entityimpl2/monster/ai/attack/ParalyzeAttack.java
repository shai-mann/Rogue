package entityimpl2.monster.ai.attack;

import entityimpl2.component.Status;
import entityimpl2.monster.Monster;
import entityimpl2.monster.ai.AbstractAttackAI;

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
        if (!player.getStatus().is(Status.Stat.PARALYZED)) {
            player.getStatus().add(Status.Stat.PARALYZED, PARALYSIS_TURNS);
            return outcome;
        }

        return Outcome.UNPERFORMED;
    }
}
