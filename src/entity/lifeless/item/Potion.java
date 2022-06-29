package entity.lifeless.item;

import entity.component.Effect;
import entity.component.Status;
import entity.lifeless.item.structure.AbstractItem;
import entity.lifeless.item.structure.Item;
import entity.monster.Monster;
import entity.player.Player;
import entity.structure.EntityProperties;
import entity.util.Obfuscator;
import map.level.Level;
import util.Helper;
import rendering.panels.gamepanes.MessageBar;

import java.awt.*;
import java.util.*;

public class Potion extends AbstractItem implements Item {

    private static final String POTION_CHARACTER = "!";
    // maps actual name to what it should be disguised as (name -> (hiddenName, isHidden))
    private enum Power {
        CONFUSION("Potion of Confusion"),
        POISON("Potion of Poison"),
        GAIN_STRENGTH("Potion of Gain Strength"),
        SEE_INVISIBLE("Potion of See Invisible"),
        HEALING("Potion of Healing"),
        MONSTER_DETECTION("Potion of Monster Detection"),
        MAGIC_DETECTION("Potion of Magic Detection"),
        RAISE_LEVEL("Potion of Raise Level"),
        EXTRA_HEALING("Potion of Extra Healing"),
        RESTORE_STRENGTH("Potion of Restore Strength"),
        BLINDNESS("Potion of Blindness");

        final String display;

        Power(String display) {
            this.display = display;
        }
    }
    private static final Obfuscator<Power> obfuscator = new Obfuscator<>(
            Arrays.asList(
                    "Blue Potion",
                    "Red Potion",
                    "Majenta Potion",
                    "Green Potion",
                    "Turquoise Potion",
                    "Pink Potion",
                    "Brown Potion",
                    "Black Potion",
                    "Orange Potion",
                    "Maroon Potion",
                    "Purple Potion"
            ), Power.values());

    private final Power power;

    public Potion(Point location) {
        super(getRandomPotionData(), location);

        power = obfuscator.read(name());
    }

    /* OVERRIDES */

    @Override
    public boolean use() {
        Level level = Level.getLevel();
        Player p = level.getPlayer();
        switch (power) {
            case CONFUSION:
                p.getStatus().add(Status.Stat.CONFUSED, Helper.random.nextInt(2) + 19);
                break;
            case POISON:
                p.getStatus().add(Status.Stat.POISONED, Helper.random.nextInt(80) + 20);
                MessageBar.addMessage("Poison flows through your bloodstream");
                break;
            case GAIN_STRENGTH:
                p.getStatus().add(Status.Stat.STRENGTHENED, Helper.random.nextInt(20));
                MessageBar.addMessage("You feel stronger");
                break;
            case SEE_INVISIBLE:
                p.getStatus().add(new Effect(Effect.Type.SEE_INVISIBLE));
                MessageBar.addMessage("Your eyesight becomes better");
                break;
            case HEALING:
                p.changeHealth(p.getLevel());
                break;
            case MAGIC_DETECTION:
                for (Item item : Level.getLevel().getItems()) {
                    if (item instanceof Wand ||
                            item instanceof Ring ||
                            item instanceof Potion ||
                            item instanceof Scroll) {
                        item.reveal();
                    }
                }
                break;
            case MONSTER_DETECTION:
                for (Monster m : Level.getLevel().getMonsters()) {
                    m.reveal();
                }
                break;
            case RAISE_LEVEL:
                p.artificiallyLevelUp();
                break;
            case EXTRA_HEALING:
                p.changeHealth(8 * p.getLevel());
                MessageBar.addMessage("You feel much better");
                break;
            case RESTORE_STRENGTH:
                p.getStatus().reset(Status.Stat.WEAKENED);
                break;
            case BLINDNESS:
                p.getStatus().add(Status.Stat.BLINDED, Helper.random.nextInt(1) + 10);
                MessageBar.addMessage("You feel your eyesight magically deteriorate");
                break;
        }

        return true;
    }

    @Override
    public String name() {
        return obfuscator.shown(power) ? power.display : super.name();
    }

    @Override
    public void identify() {
        obfuscator.setShown(power, true);

        super.identify();
    }

    public Power getPower() {
        return power;
    }

    /* STATIC METHODS */

    public static void obfuscate() {
        obfuscator.randomize();
    }

    private static EntityProperties getRandomPotionData() {
        return new EntityProperties(
                Helper.getRandom(obfuscator.getNames()),
                POTION_CHARACTER,
                Color.GREEN
        );
    }
}
