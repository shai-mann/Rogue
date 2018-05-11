package entity.lifelessentity.item;

import entity.Effect;
import entity.lifelessentity.item.combat.Armor;
import entity.lifelessentity.item.combat.Sword;
import entity.livingentity.Monster;
import entity.livingentity.Player;
import extra.MessageBar;
import extra.inventory.InventoryItem;
import extra.inventory.InventoryPane;
import helper.Helper;
import main.GameManager;
import map.level.Level;
import map.level.Room;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
            "Scroll of Enchant combat",
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

    public Scroll(int x, int y) {
        super("?", x, y);
        randomizeScrollType();
    }
    public Scroll(Scroll scroll) {
        super("?", scroll.getXPos(), scroll.getYPos());

        this.name = scroll.getName();
        this.power = scroll.power;
    }

    @Override
    public void use() {
        Player player = GameManager.getPlayer();
        switch (power) {
            case SLEEP:
                player.getStatus().setSleeping(Helper.random.nextInt(3) + 5);
                MessageBar.addMessage("You fall asleep");
                break;
            case IDENTIFY:
                InventoryPane pane = player.toggleInventory("Select an item to identify");
                pane.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            for (InventoryItem item : InventoryItem.getInventoryItems()) {
                                if (item.getPanel().getBounds().contains(e.getPoint())) {
                                    item.getItem().identify();
                                }
                            }
                        }
                    }
                });
                break;
            case ENCHANT_WEAPON:
                if (player.getHeldItem() instanceof Sword) {
                    ((Sword) player.getHeldItem()).enchant();
                    MessageBar.addMessage("Your sword glows for a moment");
                } else {
                    MessageBar.addMessage("You feel a strange sense of loss");
                }
                break;
            case REMOVE_CURSE:
                if (((Armor) player.getWornItem()).isCursed()) {
                    ((Armor) player.getWornItem()).setCursed(false);
                    MessageBar.addMessage("You feel as if somebody is watching over you");
                } else {
                    MessageBar.addMessage("You feel a strange sense of loss");
                }
                break;
            case ENCHANT_ARMOR:
                ((Armor) player.getWornItem()).setAc(((Armor) player.getWornItem()).getAc() - 1);
                MessageBar.addMessage("Your armor glows silver for a moment");
                break;
            case MAGIC_MAPPING:
                // TODO: add mapping in when ready
                break;
            case TELEPORTATION:
                player.setLocation((Room) Helper.getRandom(Room.rooms));
                break;
            case PROTECT_ARMOR:
                player.getStatus().getEffects().addEffect(Effect.PROTECT_ARMOR);
                break;
            case CREATE_MONSTER:
                if (Helper.random.nextInt(99) + 1 <= 90) {
                    Room room = Level.getLevel().getRoom(player);
                    if (room != null) {
                        Monster.createMonster(room);
                    } else {
                        MessageBar.addMessage("You hear a faint cry of anguish in the distance");
                    }
                }
                break;
        }
    }

    // SCROLL DATA RANDOMIZING

    private void randomizeScrollType() {
        hiddenName = (String) Helper.getRandom(new ArrayList(Arrays.asList(names)));
        name = "Scroll of ".concat(Helper.createRandomString(Helper.random.nextInt(5) + 10));
        power = powers.valueOf(hiddenName.split("Scroll of")[1].trim().replace(" ", "_").toUpperCase());
    }
}
