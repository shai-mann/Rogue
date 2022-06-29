package entity.structure;

import java.awt.*;

/**
 * A LivingEntity is a type of {@link Entity} that has two main additional capabilities:
 * <p>1) Movement</p>
 * <p>2) Health</p>
 */
public interface LivingEntity extends Entity {

    /**
     * Moves the {@link Entity} in by the given displacement.
     * @param displacement the amount to move the entity.
     */
    void move(Point displacement);

    void moveTo(Point newLocation);

    int health();

    void changeHealth(int delta);

    void setHealth(int health);

    int maxHealth();

    void changeMaxHealth(int delta);

    void setMaxHealth(int maxHealth);

    boolean shouldDie();

    /**
     * Left up to implementing class - should handle any necessary messages, loot drops,
     * or game ending actions taken when this entity dies.
     */
    void die();

}
