package entity.lifelessentity.item;

import entity.Effect;
import entity.livingentity.Player;
import helper.Helper;
import main.GameManager;
import util.MessageBar;

import java.util.ArrayList;
import java.util.Arrays;

public class Ring extends Item {

    private powers power;
    private int effect;

    private static ArrayList<String> obfuscatedNames = new ArrayList<>(13);
    private static String[] names = {
        "Blue Ring",
        "Red Ring",
        "Majenta Ring",
        "Green Ring",
        "Turquoise Ring",
        "Pink Ring",
        "Brown Ring",
        "Black Ring",
        "Orange Ring",
        "Maroon Ring",
        "Purple Ring",
        "Yellow Ring"
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
        super("&", GameManager.getPlayer().getXPos(), GameManager.getPlayer().getYPos());
        overWrittenGraphic = "-";
        this.name = item.getName();
        this.power = item.power;
        this.effect = item.effect;
    }

    // RING DATA RANDOMIZING

    @Override
    public void use() {
        Player p = GameManager.getPlayer();
        if (p.getRings().contains(this)) {
            remove();
            return;
        }
        p.wearRing(this);
        switch (power) {
            case PROTECTION:
                p.getStatus().getEffects().addEffect(Effect.PROTECTION);
                effect = Effect.PROTECTION;
                break;
            case SEARCHING:
                p.getStatus().getEffects().addEffect(Effect.SEARCHING);
                effect = Effect.SEARCHING;
                break;
            case SEE_INVISIBLE:
                p.getStatus().getEffects().addEffect(Effect.SEE_INVISIBLE);
                effect = Effect.SEE_INVISIBLE;
                break;
            case MAINTAIN_ARMOR:
                p.getStatus().getEffects().addEffect(Effect.PROTECT_ARMOR);
                effect = Effect.PROTECT_ARMOR;
                break;
            case SUSTAIN_STRENGTH:
                p.getStatus().getEffects().addEffect(Effect.SUSTAIN_STRENGTH);
                effect = Effect.SUSTAIN_STRENGTH;
                break;
            case STRENGTH:
                p.getStatus().getEffects().addEffect(Effect.STRENGTH);
                effect = Effect.STRENGTH;
                break;
            case REGENERATION:
                p.getStatus().getEffects().addEffect(Effect.REGENERATION);
                effect = Effect.REGENERATION;
                break;
            case AGGRAVATE_MONSTER:
                p.getStatus().getEffects().addEffect(Effect.AGGRAVATE_MONSTER);
                effect = Effect.AGGRAVATE_MONSTER;
                break;
            case SLOW_DIGESTION:
                p.getStatus().getEffects().addEffect(Effect.SLOW_DIGESTION);
                effect = Effect.SLOW_DIGESTION;
                break;
            case ADORNMENT:
                // Adornment does nothing but be worth ten gold, so it is effectively useless
                break;
            // TODO: add in ring of Dexterity and Teleportation
        }
        MessageBar.addMessage("You put on the " + getName());
    }
    public void remove() {
        GameManager.getPlayer().getInventory().add(this);
        GameManager.getPlayer().wearRing(null);
        MessageBar.addMessage("You take off the " + getName());
        GameManager.getPlayer().getStatus().getEffects().removeEffect(effect);
    }
    private void randomizeRingType() {
        hiddenName = (String) Helper.getRandom(obfuscatedNames);
        name = names[obfuscatedNames.indexOf(hiddenName)];
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

    @Override
    public String getName() {
        if (GameManager.getPlayer().getRings().contains(this)) {
            return super.getName().concat(" (held)");
        }
        return super.getName();
    }
}
