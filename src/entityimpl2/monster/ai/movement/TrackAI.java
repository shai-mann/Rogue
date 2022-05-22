package entityimpl2.monster.ai.movement;

import entityimpl2.monster.Monster;
import entityimpl2.monster.ai.AbstractMovementAI;
import entityimpl2.player.Player;
import entityimpl2.util.MoveResult;
import main.GameManager;
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
