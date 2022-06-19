package entity.lifeless.item;

import entity.component.Effect;
import entity.component.Status;
import entity.lifeless.item.combat.Weapon;
import entity.lifeless.item.structure.AbstractItem;
import entity.lifeless.item.structure.Item;
import entity.monster.Monster;
import entity.player.Player;
import entity.structure.EntityProperties;
import entity.util.Obfuscator;
import map.level.Level;
import map.level.Room;
import util.Helper;
import util.gamepanes.MessageBar;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Scroll extends AbstractItem implements Item {

    private static final String SCROLL_GRAPHIC = "?";
    private enum Power {
        MAGIC_MAPPING("Scroll of Magic Mapping"),
        SLEEP("Scroll of Sleep"),
        ENCHANT_ARMOR("Scroll of Enchant Armor"),
        IDENTIFY("Scroll of Identify"),
        TELEPORTATION("Scroll of Teleportation"),
        ENCHANT_WEAPON("Scroll of Enchant Weapon"),
        CREATE_MONSTER("Scroll of Create Monster"),
        REMOVE_CURSE("Scroll of Remove Curse"),
        PROTECT_ARMOR("Scroll of Protect Armor");

        final String display;

        Power(String display) {
            this.display = display;
        }
    }
    private static final Obfuscator<Power> obfuscator = new Obfuscator<>(
            generateObfuscatedNames(), Power.values()
    );

    private final Power power;

    public Scroll(Point location) {
        super(getRandomScrollData(), location);

        power = obfuscator.read(name());
    }

    @Override
    public boolean use() {
        Player player = Level.getLevel().getPlayer();

        switch (power) {
            case SLEEP:
                player.getStatus().add(Status.Stat.SLEEPING, Helper.random.nextInt(3) + 5);
                MessageBar.addMessage("You fall asleep");
                break;
            case IDENTIFY:
                player.inventory(/* "Select an item to identify" */);
                break;
            case ENCHANT_WEAPON:
                if (player.getHeldItem() instanceof Weapon) {
                    ((Weapon) player.getHeldItem()).enchant();
                    MessageBar.addMessage("Your weapon glows for a moment");
                } else {
                    MessageBar.addMessage("You feel a strange sense of loss");
                }
                break;
            case REMOVE_CURSE:
                if (player.getWornItem() != null && player.getWornItem().isCursed()) {
                    player.getWornItem().setCursed(false);
                    MessageBar.addMessage("You feel as if somebody is watching over you");
                } else {
                    MessageBar.addMessage("You feel a strange sense of loss");
                }
                break;
            case ENCHANT_ARMOR:
                if (player.getWornItem() != null) {
                    player.getWornItem().setAc(player.getWornItem().getAc() - 1);
                    MessageBar.addMessage("Your armor glows silver for a moment");
                } else {
                    MessageBar.addMessage("You feel a strange sense of loss");
                }
                break;
            case MAGIC_MAPPING:
                Level.getLevel().reveal();
                break;
            case TELEPORTATION:
                player.moveTo(Helper.getRandom(Room.rooms).getRandomPointInBounds());
                break;
            case PROTECT_ARMOR:
                if (player.getWornItem() != null) {
                    player.getStatus().add(new Effect(Effect.Type.PROTECT_ARMOR));
                    MessageBar.addMessage("Your armor is covered by a shimmering gold shield");
                } else {
                    MessageBar.addMessage("You feel a strange sense of loss");
                }
                break;
            case CREATE_MONSTER:
                Room room = Level.getLevel().getRoom(player);
                if (room != null) {
                    Monster.createMonster(room, Level.getLevel().getLevelNumber());
                } else {
                    MessageBar.addMessage("You hear a faint cry of anguish in the distance");
                }
                break;
        }

        obfuscator.setShown(this.power, true);
        return true;
    }

    @Override
    public String name() {
        return obfuscator.shown(power) ? power.display : super.name();
    }

    /* STATIC METHODS */

    private static EntityProperties getRandomScrollData() {
        return new EntityProperties(
                Helper.getRandom(obfuscator.getNames()),
                SCROLL_GRAPHIC,
                Color.GREEN
        );
    }

    public static void obfuscate() {
        obfuscator.randomize();
    }

    private static List<String> generateObfuscatedNames() {
        List<String> out = new ArrayList<>();

        for (int i = 0; i < Power.values().length; i++) {
            out.add(Helper.createRandomString(Helper.random.nextInt(5) + 10));
        }

        return out;
    }
}
