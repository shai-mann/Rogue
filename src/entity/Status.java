package entity;

public class Status {

    private int health = 20;
    private int paralyzed = -1;
    private int confused = 0;
    private int drunk = 0;
    private int ac = 10;
    private int weakened = 0;
    private boolean sleeping = false;
    private int sleepTurns = 0;
    private Effect effects = new Effect();

    public Status() {

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
    }
    public int getHealth() {
        return health;
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
    public Effect getEffects() {
        return effects;
    }

    public void setHealth(int health) {
        this.health = health;
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

}
