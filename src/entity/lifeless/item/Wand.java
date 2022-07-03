package entity.lifeless.item;

import entity.component.Effect;
import entity.lifeless.item.structure.AbstractItem;
import entity.lifeless.item.structure.Item;
import entity.monster.Monster;
import entity.monster.MonsterLoader;
import entity.player.Player;
import entity.structure.EntityProperties;
import entity.util.Obfuscator;
import map.Game;
import map.level.Level;
import map.level.Room;
import util.Helper;
import util.animation.Animation;
import util.gamepanes.MessageBar;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class Wand extends AbstractItem implements Item {

    private static final String WAND_GRAPHIC = "/";
    private enum Power {
        INVISIBILITY("Wand of Invisibility"),
        LIGHTNING("Wand of Lightning"),
        FIRE("Wand of Fire"),
        COLD("Wand of Cold"),
        POLYMORPH("Wand of Polymorph"),
        MAGIC_MISSILES("Wand of Magic Missiles"),
        HASTE_MONSTER("Wand of Haste Monster"),
        SLOW_MONSTER("Wand of Slow Monster"),
        DRAIN_LIFE("Wand of Drain Life"),
        NOTHING("Wand of Nothing"),
        TELEPORT_AWAY("Wand of Teleport Away"),
        TELEPORT_TO("Wand of Teleport To"),
        CANCELLATION("Wand of Cancellation");

        final String display;

        Power(String display) {
            this.display = display;
        }
    }
    private static final Obfuscator<Power> obfuscator = new Obfuscator<>(Arrays.asList(
            "Hawthorn Staff",
            "Elm Wand",
            "Redwood Wand",
            "Green Staff",
            "Elder Rod",
            "Maple Wand",
            "Juniper Staff",
            "Yew Wand",
            "Yew Staff",
            "Black Staff",
            "Elder Wand",
            "Walnut Wand",
            "Emberwood Staff"
    ), Power.values());

    private int uses = Helper.random.nextInt(2) + 8;
    private final int maxUses = uses;
    private final Power power;

    public Wand(Point location) {
        super(getRandomWandData(), location);

        power = obfuscator.read(name());
    }

    /* OVERRIDES */

    @Override
    public boolean use() {
        Player player = Level.getLevel().getPlayer();
        Optional<Monster> opt = Level.getLevel().getMonsters().stream().min(
                Comparator.comparingDouble(m -> Helper.distance(m, player))
        );

        Monster m = opt.get();

        switch (power) {
            case INVISIBILITY:
                m.attributes().setInvisible(true);
                break;
            case LIGHTNING:
                Game.getMap().getAnimationManager().addAnimation(new Animation(player.location(), Color.YELLOW));
                break;
            case FIRE:
                Game.getMap().getAnimationManager().addAnimation(new Animation(player.location(), Color.RED));
                break;
            case COLD:
                Game.getMap().getAnimationManager().addAnimation(new Animation(player.location(),
                        new Color(0, 188, 255)));
                break;
            case POLYMORPH:
                m.polymorph(Helper.getRandom(MonsterLoader.monsterClasses));
                break;
            case MAGIC_MISSILES:
                m.changeHealth(-(Helper.random.nextInt(3) + 1));
                Game.stateModel().message("Missiles fly from your wand, hitting " + m.name());
                break;
            case HASTE_MONSTER:
                if (m.attributes().speed() > 0) {
                    m.attributes().setSpeed(m.attributes().speed() + 1);
                } else {
                    m.attributes().setSpeed(1);
                }
                break;
            case SLOW_MONSTER:
                if (m.attributes().speed() < 0) {
                    m.attributes().setSpeed(m.attributes().speed() - 1);
                } else {
                    m.attributes().setSpeed(-1);
                }
            case DRAIN_LIFE:
                player.changeHealth(-player.health() / 2);
                Game.getMap().getStatusBar().updateStatusBar();
                for (Monster monster : Level.getLevel().getLoadedMonsters()) {
                    monster.changeHealth(-5);
                }
                Game.stateModel().message("All visible monsters take damage");
                break;
            case NOTHING:
                // Yup, this is legitimately a wand type.
                Game.stateModel().message("Nothing happens");
                break;
            case TELEPORT_AWAY:
                m.moveTo(Helper.getRandom(Room.rooms).getRandomPointInBounds());
                break;
            case TELEPORT_TO:
                m.moveTo(Helper.getRandom(Helper.getAdjacentPoints(player.location(), false)));
                break;
            case CANCELLATION:
                m.getStatus().add(new Effect(Effect.Type.SUPPRESS_POWER));
                Game.stateModel().message(m.name() + " loses its special abilities");
                break;
        }

        Game.stateModel().message("You used the " + name());
        uses--;

        if (uses <= 0) {
            Game.stateModel().message("Your " + name() + " breaks");
            return true;
        }

        return false;
    }

    @Override
    public String name() {
        return super.name() + " (" + uses + "/" + maxUses + ")";
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

    private static EntityProperties getRandomWandData() {
        return new EntityProperties(
                Helper.getRandom(obfuscator.getNames()),
                WAND_GRAPHIC,
                Color.GREEN
        );
    }
}
