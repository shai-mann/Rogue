package entity.livingentity.monster.ai.movement;

import entity.livingentity.Player;
import entity.livingentity.monster.Monster;
import entity.livingentity.monster.ai.AbstractMovementAI;
import main.GameManager;
import util.Helper;

import static entity.Entity.*;

public class TrackAI extends AbstractMovementAI {

    public TrackAI(Monster self) {
        super(self);
    }

    @Override
    public boolean move() {
        boolean out = super.move();

        Player player = GameManager.getPlayer();

        if (Helper.isNextTo(self, player)) {
            return out;
        }

        int direction = self.getYPos() > player.getYPos() ? UP : DOWN;
        out |= self.move(direction);

        direction = self.getXPos() < player.getXPos() ? RIGHT : LEFT;
        out |= self.move(direction);

        return out;
    }

    @Override
    public boolean shouldTriggerSecondaryAI() {
        return !Helper.isInRange(self, GameManager.getPlayer(), self.attributes().range());
    }

}
