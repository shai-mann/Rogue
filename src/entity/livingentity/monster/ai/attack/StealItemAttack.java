package entity.livingentity.monster.ai.attack;

import entity.lifelessentity.item.Item;
import entity.livingentity.monster.Monster;
import entity.livingentity.monster.ai.AbstractAttackAI;
import main.GameManager;
import util.Helper;

import java.util.List;

public class StealItemAttack extends AbstractAttackAI {

    public StealItemAttack(Monster self) {
        super(self);
    }

    @Override
    protected String successMessage() {
        return "Your backpack feels lighter";
    }

    @Override
    protected String blockedMessage() {
        return "The " + self.getName() + " fails to steal from you";
    }

    @Override
    protected Outcome performAttack(Outcome outcome) {
        List<Item> items = GameManager.getPlayer().getInventory();

        if (self.stolenItem != null || items.isEmpty()) {
            return Outcome.FAIL;
        }

        self.stolenItem = Helper.getRandom(items);
        items.remove(self.stolenItem);

        self.attributes().setMovementAI("wander", self);
        self.attributes().setAttackAI("hit", self);

        return outcome;
    }

}
