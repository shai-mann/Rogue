package entity.monster.ai.movement;

import entity.monster.Monster;
import entity.monster.ai.AbstractMovementAI;
import entity.util.MoveResult;
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
