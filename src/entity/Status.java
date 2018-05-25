package entity;

import main.GameManager;

public class Status {

    private int paralyzed = -1;
    private int confused = 0;
    private int drunk = 0;
    private int ac = 10;
    private int weakened = 0;
    private boolean sleeping = false;
    private int sleepTurns = 0;
    private int poisoned = 0;
    private int strengthened = 0;
    private Effect effects = new Effect();

    private Entity target;

    public Status(Entity e) {
        target = e;
    }
    public void update() {
        if (isParalyzed()) {
            this.paralyzed -= 1;
        }
        if (isConfused()) {
            this.confused -= 1;
        }
        if (isDrunk()) {
            this.drunk -= 1;
        }
        if (isWeakened()) {
            this.weakened -= 1;
        }
        if (sleepTurns > 0) {
            sleepTurns--;
        }
        if (isPoisoned()) {
            poisoned--;
        }
    }
    public int getHealth() {
        return target.health;
    }
    public boolean isParalyzed() {
        return paralyzed > -1;
    }
    public boolean isConfused() {
        return confused > 0;
    }
    public boolean isDrunk() {
        return drunk > 0;
    }
    public int getAc() {
        if (getEffects().hasEffect(Effect.PROTECTION)) {
            return ac + 1;
        } else {
            return ac;
        }
    }
    public boolean isWeakened() {
        return weakened > 0;
    }
    public boolean isSleeping() {
        return sleeping || sleepTurns <= 0;
    }
    public boolean isPoisoned() {
        return poisoned > 0;
    }
    public boolean isStrengthened() {
        return strengthened > 0;
    }
    public Effect getEffects() {
        return effects;
    }

    public void setHealth(int health) {
        target.health = health;
    }
    public void setParalyzed(int turns) {
        this.paralyzed = turns;
    }
    public void setConfused(int turns) {
        this.confused = turns;
    }
    public void setDrunk(int drunk) {
        this.drunk = drunk;
    }
    public void setAc(int ac) {
        if (ac >= 3) {
            this.ac = ac;
        } else {
            this.ac = 3;
        }
    }
    public void setWeakened(int weakened) {
        this.weakened = weakened;
    }
    public void setSleeping(boolean sleeping) {
        this.sleeping = sleeping;
    }
    public void setSleeping(int turns) {
        sleepTurns = turns;
    }
    public void setPoisoned(int turns) {
        this.poisoned = turns;
    }
    public void setStrengthened(int turns) {
        this.strengthened = turns;
    }

}
