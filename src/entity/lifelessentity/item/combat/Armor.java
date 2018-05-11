package entity.lifelessentity.item.combat;

import entity.lifelessentity.item.Item;
import helper.Helper;

public class Armor extends Item {

    private int ac = 10;
    private boolean cursed = false;

    public Armor(int x, int y) {
        super("]", x, y);

        randomizeArmorType();
        randomizeCursed();
    }
    public Armor(Armor item) {
        super("]", item.getXPos(), item.getYPos());

        this.name = item.getName();
        this.cursed = item.isCursed();
        this.ac = item.getAc();
    }

    // GETTERS/SETTERS

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
        cursed = Helper.random.nextBoolean();
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
