package entity.livingentity.monster.ai.movement;

import entity.livingentity.monster.Monster;
import entity.livingentity.monster.ai.AbstractMovementAI;
import main.GameManager;
import util.Helper;

import java.util.Optional;

public class MimicAI extends AbstractMovementAI {

    private boolean hiding = true;

    public MimicAI(Monster self) {
        super(self);
    }

    @Override
    public void move() {
        if (Helper.isInRange(self, GameManager.getPlayer(), 1)) {
            hiding = false; // next to player, will attack, should therefore also reveal
        }
    }

    @Override
    public Optional<String> hasOverriddenGraphic() {
        return hiding ? Optional.of("*") : super.hasOverriddenGraphic(); // pretends to be a piece of gold until it can attack
    }
}
