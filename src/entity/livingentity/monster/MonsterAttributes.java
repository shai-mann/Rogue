package entity.livingentity.monster;

import entity.livingentity.monster.ai.AIFactory;
import entity.livingentity.monster.ai.MovementAI;
import util.DiceRange;

/**
 * A mutable form of the {@link MonsterClass} record that sticks with (and mutates)
 * through a {@link Monster} entity's lifetime.
 */
public class MonsterAttributes {

    private final String name, graphic;
    private double critChance, treasureChance;
    private DiceRange hitDamage, critDamage;
    private int health, speed, range, defaultAC, experience;
    private final MovementAI movementAI, secondaryMovementAI;
    private final String attackAI; // TODO: change to AttackAI once implemented
    private boolean invisible;

    public MonsterAttributes(MonsterClass monsterClass, Monster self) {
        this.name = monsterClass.name();
        this.graphic = monsterClass.graphic();

        this.critChance = monsterClass.critChance();
        this.treasureChance = monsterClass.treasureChance();

        this.hitDamage = monsterClass.hitDamage();
        this.critDamage = monsterClass.critDamage();

        this.health = monsterClass.health().getValue();
        this.speed = monsterClass.speed();
        this.range = monsterClass.range();
        this.defaultAC = monsterClass.defaultAC();
        this.experience = monsterClass.experience();

        this.movementAI = AIFactory.constructMovementAI(monsterClass.movementAI(), self);
        this.secondaryMovementAI = AIFactory.constructMovementAI(monsterClass.secondaryMovementAI(), self);
        this.attackAI = monsterClass.attackAI();

        this.invisible = monsterClass.invisible();
    }

    public String name() {
        return name;
    }
    public String graphic() {
        return graphic;
    }
    public double critChance() {
        return critChance;
    }
    public double treasureChance() {
        return treasureChance;
    }
    public int hitDamage() {
        return hitDamage.getValue();
    }
    public int critDamage() {
        return critDamage.getValue();
    }
    public int health() {
        return health;
    }
    public int speed() {
        return speed;
    }
    public int range() {
        return range;
    }
    public int defaultAC() {
        return defaultAC;
    }
    public int experience() {
        return experience;
    }
    public MovementAI movementAI() {
        return movementAI;
    }
    public MovementAI secondaryMovementAI() {
        return secondaryMovementAI;
    }
    public String attackAI() {
        return attackAI;
    }
    public boolean isInvisible() {
        return invisible;
    }

    public void setCritChance(double critChance) {
        this.critChance = critChance;
    }
    public void setTreasureChance(double treasureChance) {
        this.treasureChance = treasureChance;
    }
    public void setHitDamage(DiceRange hitDamage) {
        this.hitDamage = hitDamage;
    }
    public void setCritDamage(DiceRange critDamage) {
        this.critDamage = critDamage;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public void setRange(int range) {
        this.range = range;
    }
    public void setDefaultAC(int defaultAC) {
        this.defaultAC = defaultAC;
    }
    public void setExperience(int experience) {
        this.experience = experience;
    }
    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

}
