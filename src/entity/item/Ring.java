package entity.item;

import helper.Helper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Ring extends Item {

    private String hiddenName;

    private static ArrayList<String> obfuscatedNames = new ArrayList<>(13);
    private static String[] colors = {
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
            "Gray Ring"
    };

    public Ring(int x, int y) {
        super("&", x, y);
        randomizeRingData();
    }
    private void randomizeRingData() {
        randomizeRingType();
    }
    public static void randomizeObfuscatedNames() {
        for (int i = 0; i < colors.length; i++) {
            String color = colors[(Helper.random.nextInt(colors.length - 1))];
            obfuscatedNames.add(color);
        }
    }
    private void randomizeRingType() {
        int ringType = Helper.random.nextInt(12);
        // TODO: Add powers to different ring types

        switch (ringType) {
            case 0:
                hiddenName = "Ring of Protection";
                break;
            case 1:
                hiddenName = "Ring of Strength";
                break;
            case 2:
                hiddenName = "Ring of Sustain Strength";
                break;
            case 3:
                hiddenName = "Ring of Searching";
                break;
            case 4:
                hiddenName = "Ring of See Invisible";
                break;
            case 5:
                hiddenName = "Ring of Adornment";
                break;
            case 6:
                hiddenName = "Ring of Aggravate Monster";
                break;
            case 7:
                hiddenName = "Ring of Dexterity";
                break;
            case 8:
                hiddenName = "Ring of Increase Damage";
                break;
            case 9:
                hiddenName = "Ring of Regeneration";
                break;
            case 10:
                hiddenName = "Ring of Slow Digestion";
                break;
            case 11:
                hiddenName = "Ring of Teleportation";
                break;
            case 12:
                hiddenName = "Ring of Stealth";
                break;
            case 13:
                hiddenName = "Ring of Maintain Armor";
                break;
        }
        name = obfuscatedNames.get(ringType);
    }
}
