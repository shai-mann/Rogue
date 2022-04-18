package entity.livingentity.monster.ai.attack;

import entity.livingentity.monster.Monster;
import entity.livingentity.monster.ai.AbstractAttackAI;
import util.Helper;

public class StealGoldAttack extends AbstractAttackAI {

    public StealGoldAttack(Monster self) {
        super(self);
    }

    @Override
    protected String successMessage() {
        return "Your pockets feel lighter";
    }

    @Override
    protected String blockedMessage() {
        return "The " + self.getName() + " couldn't take your gold";
    }

    @Override
    protected Outcome performAttack(Outcome outcome) {
        int gold = player.getGold();

        if (gold <= 0) return Outcome.BLOCKED;

        self.stolenGold += player.stealGold(Helper.getRandom(gold / 4, 50 + (gold / 4)));
        self.attributes().setMovementAI("wander", self);

        return outcome;
    }

}
