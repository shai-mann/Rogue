package entity.livingentity.monster.ai.movement;

import entity.Entity;
import entity.livingentity.monster.Monster;
import entity.livingentity.monster.ai.AbstractMovementAI;
import util.Helper;

public class WanderAI extends AbstractMovementAI {

    public WanderAI(Monster self) {
        super(self);
    }

    @Override
    public boolean move() {
        super.move();

        return self.move(Helper.getRandom(Entity.DIRECTIONS));
    }
}
