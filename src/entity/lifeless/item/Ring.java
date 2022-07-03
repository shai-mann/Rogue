package entity.lifeless.item;

import entity.component.Effect;
import entity.lifeless.item.structure.AbstractItem;
import entity.lifeless.item.structure.Item;
import entity.player.Player;
import entity.structure.EntityProperties;
import entity.util.Obfuscator;
import map.Game;
import map.level.Level;
import util.Helper;

import java.awt.*;
import java.util.Arrays;

public class Ring extends AbstractItem implements Item {

    private final static String RING_CHARACTER = "&";
    private enum Power {
        PROTECTION("Ring of Protection", new Effect(Effect.Type.PROTECTION)),
        STRENGTH("Ring of Strength", new Effect(Effect.Type.STRENGTH)),
        SUSTAIN_STRENGTH("Ring of Sustain Strength", new Effect(Effect.Type.SUSTAIN_STRENGTH)),
        SEARCHING("Ring of Searching", new Effect(Effect.Type.SEARCHING)),
        SEE_INVISIBLE("Ring of See Invisible", new Effect(Effect.Type.SEE_INVISIBLE)),
        ADORNMENT("Ring of Adornment", null),
        AGGRAVATE_MONSTER("Ring of Aggravate Monster", new Effect(Effect.Type.AGGRAVATE_MONSTER)),
        DEXTERITY("Ring of Dexterity", null),
        REGENERATION("Ring of Regeneration", new Effect(Effect.Type.REGENERATION)),
        SLOW_DIGESTION("Ring of Slow Digestion", new Effect(Effect.Type.SLOW_DIGESTION)),
        TELEPORTATION("Ring of Teleportation", null),
        MAINTAIN_ARMOR("Ring of Maintain Armor", new Effect(Effect.Type.PROTECT_ARMOR));

        final String display;
        final Effect effect;

        Power(String display, Effect effect) {
            this.display = display;
            this.effect = effect;
        }
    }
    private final static Obfuscator<Power> obfuscator = new Obfuscator<>(
            Arrays.asList(
                    "Blue Ring",
                    "Red Ring",
                    "Magenta Ring",
                    "Green Ring",
                    "Turquoise Ring",
                    "Pink Ring",
                    "Brown Ring",
                    "Black Ring",
                    "Orange Ring",
                    "Maroon Ring",
                    "Purple Ring",
                    "Yellow Ring"
            ), Power.values()
    );

    private final Power power;

    public Ring(Point location) {
        super(randomizeRingData(), location);

        power = obfuscator.read(name());
    }

    /* OVERRIDES */

    @Override
    public boolean use() {
        Player p = Level.getLevel().getPlayer();

        if (p.isWorn(this)) {
            p.remove(this);
            p.getStatus().remove(power.effect);
            Game.stateModel().message("You take off the " + name());
            return false;
        }

        if (p.wear(this)) {
            if (power.effect != null) {
                p.getStatus().add(power.effect);
            }
            Game.stateModel().message("You put on the " + name());
        } else {
            Game.stateModel().message("You cannot wear more than 2 rings");
        }

        return false;
    }

    @Override
    public String name() {
        String name = obfuscator.shown(power) ? power.display : super.name();

        if (Level.getLevel().getPlayer().isWorn(this)) {
            name += " (worn)";
        }
        return name;
    }

    @Override
    public void identify() {
        obfuscator.setShown(power, true);

        super.identify();
    }

    /* STATIC METHODS */

    public static void obfuscate() {
        obfuscator.randomize();
    }

    private static EntityProperties randomizeRingData() {
        return new EntityProperties(
                Helper.getRandom(obfuscator.getNames()),
                RING_CHARACTER,
                Color.GREEN
        );
    }
}
