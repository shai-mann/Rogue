package entity.monster.ai.attack;

import entity.lifeless.item.structure.Item;
import entity.monster.Monster;
import entity.monster.ai.AbstractAttackAI;
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
        return "The " + self.name() + " fails to steal from you";
    }

    @Override
    protected Outcome performAttack(Outcome outcome) {
        List<Item> items = player.getInventory();

        if (self.inventory.hasItems() || items.isEmpty()) {
            return Outcome.FAIL;
        }

        Item item = Helper.getRandom(items);
        self.inventory.addItems(item);
        items.remove(item);

        self.attributes().setMovementAI("wander", self);
        self.attributes().setAttackAI("hit", self);

        return outcome;
    }

}
