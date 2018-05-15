package entity.lifelessentity.item;

import helper.Helper;

import java.util.ArrayList;
import java.util.Arrays;

public class Ring extends Item {

    private powers power;

    private static ArrayList<String> obfuscatedNames = new ArrayList<>(13);
    private static String[] names = {
        "Blue Ring",
        "Red Ring",
        "Majenta Ring",
        "Green Ring",
        "Turquoise Ring",
        "Yellow Ring",
        "Pink Ring",
        "Brown Ring",
        "Black Ring",
        "Orange Ring",
        "Maroon Ring",
        "Purple Ring",
    };
    private static String[] hiddenNames = {
        "Ring of Protection",
        "Ring of Strength",
        "Ring of Sustain Strength",
        "Ring of Searching",
        "Ring of See Invisible",
        "Ring of Adornment",
        "Ring of Aggravate Monster",
        "Ring of Dexterity",
        "Ring of Increase Damage",
        "Ring of Regeneration",
        "Ring of Slow Digestion",
        "Ring of Teleportation",
        "Ring of Maintain Armor"
    };
    private enum powers {
        PROTECTION,
        STRENGTH,
        SUSTAIN_STRENGTH,
        SEARCHING,
        SEE_INVISIBLE,
        ADORNMENT,
        AGGRAVATE_MONSTER,
        DEXTERITY,
        INCREASE_DAMAGE,
        REGENERATION,
        SLOW_DIGESTION,
        TELEPORTATION,
        MAINTAIN_ARMOR
    }

    public Ring(int x, int y) {
        super("&", x, y);
        randomizeRingType();
    }
    public Ring(Ring item) {
        super("&", item.getXPos(), item.getYPos());
        this.name = item.getName();
    }

    // RING DATA RANDOMIZING

    private void randomizeRingType() {
        name = (String) Helper.getRandom(obfuscatedNames);
        hiddenName = hiddenNames[obfuscatedNames.indexOf(name)];
        power = powers.valueOf(hiddenName.split("Ring of")[1].trim().replace(" ", "_").toUpperCase());
    }
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
