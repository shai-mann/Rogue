package entity.structure;

import util.Helper;

import java.awt.*;

public abstract class AbstractLivingEntity extends AbstractEntity implements LivingEntity {

    private int maxHealth, health;

    public AbstractLivingEntity(EntityProperties properties, Point location, int health) {
        this(properties, location, health, health);
    }

    public AbstractLivingEntity(EntityProperties properties, Point location, int health, int maxHealth) {
        super(properties, location);

        this.maxHealth = maxHealth;
        this.health = health;
    }

    @Override
    public void move(Point displacement) {
        this.location = Helper.translate(location(), displacement);

        System.out.println("[DEBUG] " + name() + " moved to location (" + location().x + ", " + location().y + ")");
    }

    @Override
    public void moveTo(Point newLocation) {
        this.location.setLocation(newLocation);
    }

    @Override
    public int health() {
        return health;
    }

    @Override
    public void changeHealth(int delta) {
        health += delta;

        health = Math.min(health, maxHealth); // cap out health at max health.
    }

    @Override
    public void setHealth(int health) {
        this.health = Math.min(health, maxHealth);
    }

    @Override
    public int maxHealth() {
        return maxHealth;
    }

    @Override
    public void changeMaxHealth(int delta) {
        maxHealth += delta;
    }

    @Override
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    @Override
    public boolean shouldDie() {
        return health <= 0;
    }
}
