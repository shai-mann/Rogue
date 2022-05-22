package entityimpl2.monster.ai.attack;

import entityimpl2.component.Effect;
import entityimpl2.component.Status;
import entityimpl2.monster.Monster;
import entityimpl2.monster.ai.AbstractAttackAI;
import util.Helper;

public class WeakenAttack extends AbstractAttackAI {

    private static final int WEAKENED_TURNS_MIN = 2, WEAKENED_TURN_MAX = 12;

    public WeakenAttack(Monster self) {
        super(self);
    }

    @Override
    protected String successMessage() {
        return "You feel weaker";
    }

    @Override
    protected String blockedMessage() {
        return "The " + self.name() + "'s attack was magically deflected";
    }

    @Override
    protected Outcome performAttack(Outcome outcome) {
        if (player.getStatus().hasEffect(Effect.Type.SUSTAIN_STRENGTH)) return Outcome.BLOCKED;

        player.getStatus().add(Status.Stat.WEAKENED, Helper.getRandom(WEAKENED_TURNS_MIN, WEAKENED_TURN_MAX));
        return outcome;
    }
}
