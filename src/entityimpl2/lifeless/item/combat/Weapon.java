package entityimpl2.lifeless.item.combat;

import entityimpl2.lifeless.item.structure.AbstractItem;
import entityimpl2.lifeless.item.structure.Item;
import entityimpl2.player.Player;
import entityimpl2.structure.EntityProperties;
import map.level.Level;
import util.Helper;
import util.gamepanes.MessageBar;

import java.awt.*;

public class Weapon extends AbstractItem implements Item {

    private static final String WEAPON_GRAPHIC = ")";
    private static final String WEAPON_NAME = "Weapon"; // todo: change when adding weapon loading

    private int damage = 1;
    private boolean throwable = false;
    private boolean isBow = false;
    private boolean cursed = false;
    private int throwDamage;
    private int amount = 1;

    public Weapon(Point location) {
        super(new EntityProperties(WEAPON_NAME, WEAPON_GRAPHIC, Color.GREEN), location);

        if (damage <= 1) {
            damage = 2;
        }
    }

    /* OVERRIDES */

    @Override
    public boolean usable() {
        return true;
    }

    @Override
    public boolean use() {
        Player p = Level.getLevel().getPlayer();

        if (p.getHeldItem().equals(this)) {
            p.setHeldItem(null);
            MessageBar.addMessage("You put away the " + name());
            return false;
        }

        p.setHeldItem(this);
        MessageBar.addMessage("You hold the " + name());

        return false;
    }
    @Override
    public String name() {
        return isHeld() ? super.name().concat(" (held)") : super.name();
    }

    /* GETTERS / SETTERS */

    private int getMaxDamage() {
        return damage;
    }
    public void enchant() {
        damage++;
    }
    public boolean isCursed() {
        return cursed;
    }
    public int getDamage() {
        return Helper.random.nextInt(damage - 1) + 1;
    }
    public int getThrowDamage() {
        return Helper.random.nextInt(throwDamage) + 1;
    }
    public boolean isBow() {
        return isBow;
    }
    public boolean isThrowable() {
        return throwable;
    }
    public boolean isHeld() {
        return this.equals(Level.getLevel().getPlayer().getHeldItem());
    }
}
