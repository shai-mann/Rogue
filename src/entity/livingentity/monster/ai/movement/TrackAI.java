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
        super.move();

        Player player = GameManager.getPlayer();

        if (Helper.isInRange(self, player, self.attributes().range())) {
            if (!Helper.isNextTo(self, player)) {
                int direction = self.getYPos() > player.getYPos() ? UP : DOWN;
                self.move(direction);

                direction = self.getXPos() < player.getXPos() ? RIGHT : LEFT;
                self.move(direction);
            }
        } else {
            shouldTriggerSecondaryMovementAI = true;
        }
    }

}
