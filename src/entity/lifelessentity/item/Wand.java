package entity.lifelessentity.item;

import util.MessageBar;
import helper.Helper;

import java.util.ArrayList;
import java.util.Arrays;

public class Wand extends Item {

    private int uses = Helper.random.nextInt(2) + 8;
    private powers power;

    private static ArrayList<String> obfuscatedNames = new ArrayList<>(13);
    private static String[] names = {
            "Hawthorn Wand",
            "Elm Wand",
            "Redwood Wand",
            "Green Staff",
            "Elder Rod",
            "Red Staff",
            "Maple Wand",
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
