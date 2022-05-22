package entity.monster.ai.attack;

import entity.monster.Monster;
import entity.monster.ai.AbstractAttackAI;
import util.Helper;

public class StealGoldAttack extends AbstractAttackAI {

    // TODO: update this and stealItem attack to change to RUN movement type after stealing
    public StealGoldAttack(Monster self) {
        super(self);
    }

    @Override
    protected String successMessage() {
        return "Your pockets feel lighter";
    }

    @Override
    protected String blockedMessage() {
        return "The " + self.name() + " couldn't take your gold";
    }

    @Override
    protected Outcome performAttack(Outcome outcome) {
        int gold = player.getGold();

        if (gold <= 0) return Outcome.BLOCKED;

        int stealAmount = Helper.getRandom(gold / 4, 50 + (gold / 4));

        self.inventory.changeGold(player.changeGold(stealAmount));
        self.attributes().setMovementAI("wander", self);

        return outcome;
    }

}
