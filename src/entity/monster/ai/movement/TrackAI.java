package entity.monster.ai.movement;

import entity.monster.Monster;
import entity.monster.ai.AbstractMovementAI;
import entity.player.Player;
import entity.util.MoveResult;
import map.level.Level;
import util.Helper;

import java.awt.*;

public class TrackAI extends AbstractMovementAI {

    public TrackAI(Monster self) {
        super(self);
    }

    @Override
    public MoveResult move() {
        super.move();

        Player player = Level.getLevel().getPlayer();
        if (Helper.isNextTo(self, player)) {
            return new MoveResult(player); // returns the player so that the player gets hit
        }

        Point displacement = new Point(
                self.location().y > player.location().y ? -1 : 1,
                self.location().x > player.location().x ? -1 : 1
        );

        return attemptMove(displacement);
    }

    @Override
    public boolean shouldTriggerSecondaryAI() {
        return !Helper.isInRange(self, Level.getLevel().getPlayer(), self.attributes().range());
    }

}
