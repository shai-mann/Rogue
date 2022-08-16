package entity.lifeless.item.combat;

import entity.lifeless.item.structure.AbstractItem;
import entity.lifeless.item.structure.Item;
import entity.player.Player;
import entity.structure.EntityProperties;
import map.Game;
import map.level.Level;
import util.Helper;

import java.awt.*;
import java.util.Arrays;

public class Armor extends AbstractItem implements Item {

    private enum Type {
        LEATHER("Leather Armor", 8),
        RING_MAIL("Ring Mail", 7),
        STUDDED_LEATHER("Studded Leather", 7),
        SCALE("Scale Mail", 6),
        CHAIN("Chain Mail", 5),
        SPLINT("Splint Mail", 4),
        BANDED("Banded Mail", 4),
        PLATE("Plate Mail", 3);

        String name;
        int ac;

        Type(String name, int ac) {
            this.name = name;
            this.ac = ac;
        }
    }
    private static final String ARMOR_GRAPHIC = "]";

    private final Type type;
    private boolean cursed;

    public Armor(Point location) {
        this(randomArmorType(), curse(), location);
    }

    public Armor(Type type, boolean cursed, Point location) {
        super(new EntityProperties(type.name, ARMOR_GRAPHIC, Color.GREEN), location);

        this.type = type;
        this.cursed = cursed;
    }

    /* OVERRIDES */

    @Override
    public boolean usable() {
        return true;
    }

    @Override
    public boolean use() {
        Player player = Level.getLevel().getPlayer();
        if (isBeingWorn()) {
            player.setWornItem(null);
            Game.stateModel().message("You remove the " + name());
            return false;
        }
        if (!(player.getWornItem() != null && player.getWornItem().isCursed())) {
            Game.stateModel().message("You equip the " + name());
            player.setWornItem(this);
        } else {
            Game.stateModel().message("You cannot take off the armor you are wearing");
        }

        return false; // never removed unless dropped
    }

    @Override
    public String name() {
        return isBeingWorn() ? super.name().concat(" (worn)") : super.name();
    }

    /* GENERATE FIELDS */

    public static Type randomArmorType() {
        return Helper.getRandom(Arrays.asList(Type.values()));
    }

    public static boolean curse() {
        return Helper.random.nextInt(99) + 1 >= 95;
    }

    /* GETTERS/SETTERS */

    public boolean isBeingWorn() {
        return this.equals(Level.getLevel().getPlayer().getWornItem());
    }

    public boolean isCursed() {
        return cursed;
    }

    public int getAc() {
        return type.ac;
    }

    public void setCursed(boolean cursed) {
        this.cursed = cursed;
    }

    public void setAc(int ac) {
        this.type.ac = ac;
    }
}
