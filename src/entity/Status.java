package entity;

public class Status {

    private int health = 20;
    private int paralyzed = 0;
    private int confused = 0;
    private int drunk = 0;
    private int ac = 10;
    private int weakened = 0;
    private int sleeping = 0;


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
        if (isSleeping()) {
            this.sleeping -= 1;
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
    public int getAc() { return ac; }
    public boolean isWeakened() {
        return weakened > 0;
    }
    public boolean isSleeping() {
        return sleeping > 0;
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
    public void setAc(int ac) { this.ac = ac; }
    public void setWeakened(int weakened) {
        this.weakened = weakened;
    }
    public void setSleeping(int sleeping) {
        this.sleeping += sleeping;
    }

}
