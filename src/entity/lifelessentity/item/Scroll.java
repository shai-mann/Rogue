package entity.lifelessentity.item;

import entity.Effect;
import entity.lifelessentity.item.combat.Weapon;
import entity.livingentity.Monster;
import entity.livingentity.Player;
import util.gamepanes.MessageBar;
import helper.Helper;
import main.GameManager;
import map.level.Level;
import map.level.Room;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Scroll extends Item {

    private powers power;

    private static String[] names = {
            "Scroll of Magic Mapping",
            "Scroll of Sleep",
            "Scroll of Enchant Armor",
            "Scroll of Identify",
            "Scroll of Teleportation",
            "Scroll of Enchant Weapon",
            "Scroll of Create Monster",
            "Scroll of Remove Curse",
            "Scroll of Protect Armor"
    };
    private enum powers {
        MAGIC_MAPPING,
        SLEEP,
        ENCHANT_ARMOR,
        IDENTIFY,
        TELEPORTATION,
        ENCHANT_WEAPON,
        CREATE_MONSTER,
        REMOVE_CURSE,
        PROTECT_ARMOR
    }
    public static ArrayList<powers> revealedTypes = new ArrayList<>();

    public Scroll(int x, int y) {
        super("?", x, y);
        randomizeScrollType();
    }
    public Scroll(Scroll scroll) {
        super("?", GameManager.getPlayer().getXPos(), GameManager.getPlayer().getYPos());
        overWrittenGraphic = "-";
        this.name = scroll.getName();
        this.power = scroll.power;
        this.hiddenName = scroll.getHiddenName();
    }

    @Override
    public void use() {
        Player player = GameManager.getPlayer();
        player.getInventory().remove(this);
        switch (power) {
            case SLEEP:
                player.getStatus().setSleeping(Helper.random.nextInt(3) + 5);
                MessageBar.addMessage("You fall asleep");
                break;
            case IDENTIFY:
                player.toggleInventory("Select an item to identify");
                break;
            case ENCHANT_WEAPON:
                if (player.getHeldItem() instanceof Weapon) {
                    ((Weapon) player.getHeldItem()).enchant();
                    MessageBar.addMessage("Your sword glows for a moment");
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
                for (int i = 0; i < GameManager.getTable().getRowCount(); i++) {
                    for (int j = 0; j < GameManager.getTable().getColumnCount(); j++) {
                        Level.getLevel().addShownPoint(new Point(j, i));
                    }
                }
                break;
            case TELEPORTATION:
                GameManager.getTable().setValueAt(player.overWrittenGraphic, player.getYPos(), player.getXPos());
                player.setLocation((Room) Helper.getRandom(Room.rooms));
                break;
            case PROTECT_ARMOR:
                if (player.getWornItem() != null) {
                    player.getStatus().getEffects().addEffect(Effect.PROTECT_ARMOR);
                    MessageBar.addMessage("Your armor is covered by a shimmering gold shield");
                } else {
                    MessageBar.addMessage("You feel a strange sense of loss");
                }
                break;
            case CREATE_MONSTER:
                Room room = Level.getLevel().getRoom(player);
                if (room != null) {
                    Monster.createMonster(room);
                } else {
                    MessageBar.addMessage("You hear a faint cry of anguish in the distance");
                }
                break;
        }
        revealedTypes.add(this.power);
    }

    @Override
    public String getName() {
        return revealedTypes.contains(power) ? hiddenName : super.getName();
    }
    public powers getPower() {
        return power;
    }

    // SCROLL DATA RANDOMIZING

    private void randomizeScrollType() {
        hiddenName = (String) Helper.getRandom(new ArrayList(Arrays.asList(names)));
        name = "Scroll of ".concat(Helper.createRandomString(Helper.random.nextInt(5) + 10));
        power = powers.valueOf(hiddenName.split("Scroll of")[1].trim().replace(" ", "_").toUpperCase());
    }
}
