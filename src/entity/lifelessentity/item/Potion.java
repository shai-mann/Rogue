package entity.lifelessentity.item;

import entity.Effect;
import entity.livingentity.Monster;
import entity.livingentity.Player;
import helper.Helper;
import main.GameManager;
import map.level.Level;
import util.gamepanes.MessageBar;

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
            "Pink Potion",
            "Brown Potion",
            "Black Potion",
            "Orange Potion",
            "Maroon Potion",
            "Purple Potion"
    };
    private static String[] hiddenNames = {
            "Potion of Confusion",
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
        Player p = GameManager.getPlayer();
        switch (power) {
            case CONFUSION:
                p.getStatus().setConfused(Helper.random.nextInt(2) + 19);
                break;
            case POISON:
                p.getStatus().setPoisoned(Helper.random.nextInt(80) + 20);
                MessageBar.addMessage("Poison flows through your bloodstream");
                break;
            case GAIN_STRENGTH:
                p.getStatus().setStrengthened(Helper.random.nextInt(20));
                MessageBar.addMessage("You feel stronger");
                break;
            case SEE_INVISIBLE:
                p.getStatus().getEffects().addEffect(Effect.SEE_INVISIBLE);
                MessageBar.addMessage("Your eyesight becomes better");
                break;
            case HEALING:
                p.getStatus().setHealth(p.getHealth() + p.getLevel());
                break;
            case MAGIC_DETECTION:
                for (Item item : Item.items) {
                    if (item instanceof Wand ||
                            item instanceof Ring ||
                            item instanceof Potion ||
                            item instanceof Scroll) {
                        GameManager.getTable().setValueAt(item.graphic, item.getYPos(), item.getXPos());
                    }
                }
                break;
            case MONSTER_DETECTION:
                for (Monster m : Monster.getMonsters()) {
                    GameManager.getTable().setValueAt(m.graphic, m.getYPos(), m.getXPos());
                }
                break;
            case RAISE_LEVEL:
                p.addExperience(p.getLevelThreshold() + 1);
                break;
            case EXTRA_HEALING:
                p.getStatus().setHealth(p.getHealth() + (8 * p.getLevel()));
                MessageBar.addMessage("You feel much better");
                break;
            case RESTORE_STRENGTH:
                p.getStatus().setWeakened(0);
                break;
            case BLINDNESS:
                GameManager.getPlayer().getStatus().setBlinded(Helper.random.nextInt(1) + 10);
                Level.getLevel().blind();
                MessageBar.addMessage("You feel your eyesight magically deteriorate");
                break;
        }
        p.getInventory().remove(this);
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
