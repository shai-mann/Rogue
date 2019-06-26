package entity.lifelessentity.item.combat;

import entity.lifelessentity.item.Item;
import entity.livingentity.Player;
import helper.Helper;
import main.GameManager;
import util.gamepanes.MessageBar;

public class Armor extends Item {

    private int ac = 10;
    private boolean cursed = false;

    public Armor(int x, int y) {
        super("]", x, y);

        randomizeArmorType();
        randomizeCursed();
    }
    public Armor(Armor item) {
        super("]", GameManager.getPlayer().getXPos(), GameManager.getPlayer().getYPos());
        overWrittenGraphic = "-";
        this.name = item.getName();
        this.cursed = item.isCursed();
        this.ac = item.getAc();
    }

    // OVERRIDES

    @Override
    public void use() {
        Player player = GameManager.getPlayer();
        if (isBeingWorn()) {
            player.setWornItem(null);
            MessageBar.addMessage("You remove the " + getName());
            return;
        }
        if (!(player.getWornItem() != null && player.getWornItem().isCursed())) {
            player.setWornItem(this);
            MessageBar.addMessage("You equip the " + getName());
        } else {
            MessageBar.addMessage("You cannot take off the armor you are wearing");
        }
    }
    @Override
    public String getName() {
        if (isBeingWorn()) {
            return name.concat(" (worn)");
        } else {
            return super.getName();
        }
    }

    // GETTERS/SETTERS

    public boolean isBeingWorn() {
        return this.equals(GameManager.getPlayer().getWornItem());
    }
    private void randomizeArmorType() {
        int type = Helper.random.nextInt(7) + 1; // TODO: Make this based on level or percentages

        switch (type) {
            case 1:
                name = "Leather Armor";
                ac = 8;
                break;
            case 2:
                name = "Ring Mail";
                ac = 7;
                break;
            case 3:
                name = "Studded Leather";
                ac = 7;
                break;
            case 4:
                name = "Scale Mail";
                ac = 6;
                break;
            case 5:
                name = "Chain Mail";
                ac = 5;
                break;
            case 6:
                name = "Splint Mail";
                ac = 4;
                break;
            case 7:
                name = "Banded Mail";
                ac = 4;
                break;
            case 8:
                name = "Plate Mail";
                ac = 3;
                break;
        }
    }
    private void randomizeCursed() {
        cursed = Helper.random.nextInt(99) + 1 >= 90;
    }
    public boolean isCursed() {
        return cursed;
    }
    public int getAc() {
        return ac;
    }
    public void setCursed(boolean cursed) {
        this.cursed = cursed;
    }
    public void setAc(int ac) {
        this.ac = ac;
    }
}
