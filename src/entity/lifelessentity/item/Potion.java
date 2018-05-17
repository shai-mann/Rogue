package entity.lifelessentity.item;

import helper.Helper;
import main.GameManager;

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
        super("!", GameManager.getPlayer().getXPos(), GameManager.getPlayer().getYPos());
        overWrittenGraphic = "-";
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
