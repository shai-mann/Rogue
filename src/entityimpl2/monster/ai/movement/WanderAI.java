package entityimpl2.monster.ai.movement;

import entityimpl2.monster.Monster;
import entityimpl2.monster.ai.AbstractMovementAI;
import entityimpl2.util.MoveResult;
import util.Helper;

import java.awt.*;

public class WanderAI extends AbstractMovementAI {

    public WanderAI(Monster self) {
        super(self);
    }

    @Override
    public MoveResult move() {
        super.move();

        Point displacement = Helper.getRandom(Helper.getAdjacentPoints(new Point(0, 0), false));
        return attemptMove(displacement);
    }
}
