package entity.lifelessentity.item;

import helper.Helper;

import java.util.ArrayList;
import java.util.Arrays;

public class Potion extends Item {

    private powers power;

    private static ArrayList<String> obfuscatedNames = new ArrayList<>(13);
    private static String[] names = {
            "Blue Potion",
            "Red Potion",
            "Majenta Potion",
            "Green Potion",
            "Turquoise Potion",
            "Yellow Potion",
            "Pink Potion",
            "Brown Potion",
            "Black Potion",
            "Orange Potion",
            "Maroon Potion",
            "Purple Potion"
    };
    private static String[] hiddenNames = {
            "Potion of Confusion",
            "Potion of Hallucination",
            "Potion of Poison",
            "Potion of Gain Strength",
            "Potion of See Invisible",
            "Potion of Healing",
            "Potion of Monster Detection",
            "Potion of Magic Detection",
            "Potion of Raise Level",
            "Potion of Extra Healing",
            "Potion of Restore Strength",
            "Potion of Blindness"
    };
    private enum powers {
        CONFUSION,
        HALLUCINATION,
        POISON,
        GAIN_STRENGTH,
        SEE_INVISIBLE,
        HEALING,
        MONSTER_DETECTION,
        MAGIC_DETECTION,
        RAISE_LEVEL,
        EXTRA_HEALING,
        RESTORE_STRENGTH,
        BLINDNESS
    }

    public Potion(int x, int y) {
        super("!", x, y);
        randomizePotionData();
    }
    public Potion(Potion potion) {
        super("!", potion.getXPos(), potion.getYPos());
        name = potion.getName();
        hiddenName = potion.getHiddenName();
        power = potion.getPower();
    }

    private void randomizePotionData() {
        hiddenName = (String) Helper.getRandom(new ArrayList(Arrays.asList(hiddenNames)));
        name = names[new ArrayList<>(Arrays.asList(hiddenNames)).indexOf(hiddenName)];
        power = powers.valueOf(hiddenName.split("Potion of")[1].trim().replace(" ", "_").toUpperCase());
    }
    @Override
    public void use() {

    }
    public powers getPower() {
        return power;
    }

    // STATIC METHODS

    public static void randomizeObfuscatedNames() {
        for (int i = 0; i < hiddenNames.length; i++) {
            String color = hiddenNames[(Helper.random.nextInt(hiddenNames.length - 1))];
            obfuscatedNames.add(color);
        }
    }
}
