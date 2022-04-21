package entity.livingentity.monster.ai.attack;

import entity.component.Effect;
import entity.livingentity.monster.Monster;
import entity.livingentity.monster.ai.AbstractAttackAI;
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
        return "The " + self.getName() + "'s attack was magically deflected";
    }

    @Override
    protected Outcome performAttack(Outcome outcome) {
        if (player.getStatus().hasEffect(Effect.SUSTAIN_STRENGTH)) return Outcome.BLOCKED;

        player.getStatus().setWeakened(Helper.getRandom(WEAKENED_TURNS_MIN, WEAKENED_TURN_MAX));
        return outcome;
    }
}
