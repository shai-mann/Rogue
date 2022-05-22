package entity.monster.ai.movement;

import entity.lifeless.item.Gold;
import entity.monster.Monster;
import entity.monster.ai.AbstractMovementAI;
import entity.util.MoveResult;
import map.level.Level;
import util.Helper;

import java.util.Optional;

public class MimicAI extends AbstractMovementAI {

    private boolean hiding = true;

    public MimicAI(Monster self) {
        super(self);
    }

    @Override
    public MoveResult move() {
        MoveResult out = super.move();

        if (Helper.isNextTo(self, Level.getLevel().getPlayer())) {
            hiding = false; // next to player, will attack, should therefore also reveal
        }

        return out;
    }

    @Override
    public boolean shouldTriggerSecondaryAI() {
        return !hiding;
    }

    @Override
    public Optional<String> hasOverriddenGraphic() {
        return hiding ? Optional.of(Gold.GOLD_GRAPHIC) : super.hasOverriddenGraphic();
    }
}
