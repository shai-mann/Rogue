package entity;

public class Status {

    private int health = 20;
    private int paralyzed = 0;
    private int confused = 0;
    private int drunk = 0;
    private int ac = 10;

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

}
