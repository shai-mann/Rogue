package entity.livingentity.monster.ai.movement;

import entity.Entity;
import entity.livingentity.monster.Monster;
import entity.livingentity.monster.ai.AbstractMovementAI;
import util.Helper;

public class WanderAI extends AbstractMovementAI {

    public WanderAI() {}

    public WanderAI(Monster self) {
        super(self);
    }

    @Override
    public void move() {
        super.move();

        self.move(Helper.getRandom(Entity.DIRECTIONS));
    }
}
