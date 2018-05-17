package entity.lifelessentity.item;

import entity.Effect;
import entity.livingentity.Monster;
import main.GameManager;
import map.Map;
import map.level.Room;
import util.MessageBar;
import helper.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Wand extends Item {

    private int uses = Helper.random.nextInt(2) + 8;
    private powers power;

    private static ArrayList<String> obfuscatedNames = new ArrayList<>(13);
    private static String[] names = {
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
    };
    private static String[] hiddenNames = {
            "Wand of Invisibility",
            "Wand of Lightning",
            "Wand of Fire",
            "Wand of Cold",
            "Wand of Polymorph",
            "Wand of Magic Missiles",
            "Wand of Haste Monster",
            "Wand of Slow Monster",
            "Wand of Drain Life",
            "Wand of Nothing",
            "Wand of Teleport Away",
            "Wand of Teleport To",
            "Wand of Cancellation"
    };
    private enum powers {
        INVISIBILITY,
        LIGHTNING,
        FIRE,
        COLD,
        POLYMORPH,
        MAGIC_MISSILES,
        HASTE_MONSTER,
        SLOW_MONSTER,
        DRAIN_LIFE,
        NOTHING,
        TELEPORT_AWAY,
        TELEPORT_TO,
        CANCELLATION
    }

    public Wand(int x, int y) {
        super("/", x, y);
        randomizeWandData();
    }
    public Wand(Wand wand) {
        super("/", wand.getXPos(), wand.getYPos());
        name = wand.getName();
        hiddenName = wand.getHiddenName();
        power = wand.getPower();
    }

    // RANDOMIZE WAND DATA

    private void randomizeWandData() {
        hiddenName = (String) Helper.getRandom(obfuscatedNames);
        name = names[obfuscatedNames.indexOf(hiddenName)];
        power = powers.valueOf(hiddenName.split("Wand of")[1].trim().replace(" ", "_").toUpperCase());
    }
    @Override
    public void use() {
        Monster m = Monster.getClosestMonster(GameManager.getPlayer());
        switch (power) {
            case INVISIBILITY:
                m.setInvisible(true);
                GameManager.getTable().setValueAt(m.overWrittenGraphic, m.getYPos(), m.getXPos());
                break;
            case LIGHTNING:
                // TODO: implement Lightning, fire, and cold wand powers
                break;
            case FIRE:
                break;
            case COLD:
                break;
            case POLYMORPH:
                m.setType((File) Helper.getRandom(new ArrayList(Arrays.asList(new File("data/monsters").listFiles()))));
                break;
            case MAGIC_MISSILES:
                break;
            case HASTE_MONSTER:
                if (m.getSpeed() > 0) {
                    m.setSpeed(m.getSpeed() + 1);
                } else {
                    m.setSpeed(1);
                }
                break;
            case SLOW_MONSTER:
                if (m.getSpeed() < 0) {
                    m.setSpeed(m.getSpeed() - 1);
                } else {
                    m.setSpeed(-1);
                }
            case DRAIN_LIFE:
                int damage = GameManager.getPlayer().getHealth() / 2;
                GameManager.getPlayer().getStatus().setHealth(damage);
                Map.getMap().getStatusBar().updateStatusBar();
                // TODO: once visible vs. not visible monsters implemented, make all visible ones lose damage amount health
                break;
            case NOTHING:
                // Yup, this is legitimately a wand type.
                break;
            case TELEPORT_AWAY:
                m.setLocation(((Room) Helper.getRandom(Room.rooms)).getRandomPointInBounds());
                break;
            case TELEPORT_TO:
                m.setLocation(GameManager.getPlayer().getPointNextTo());
                break;
            case CANCELLATION:
                m.getStatus().getEffects().addEffect(Effect.SUPPRESS_POWER);
                break;
        }
        uses--;
        if (uses <= 0) {
            Item.items.remove(this);
            MessageBar.addMessage("Your " + name + " breaks");
        }
    }
    private powers getPower() {
        return power;
    }

    // STATIC METHODS

    public static void randomizeObfuscatedNames() {
        ArrayList<String> temp = new ArrayList<>(Arrays.asList(hiddenNames));
        for (int i = 0; i < temp.size();) {
            String name;
            if (temp.size() == 1) {
                name = temp.get(Helper.random.nextInt(temp.size()));
            } else {
                name = temp.get(Helper.random.nextInt(temp.size() - 1));
            }
            temp.remove(name);
            obfuscatedNames.add(name);
        }
    }
}
