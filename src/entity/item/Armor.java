package entity.item;

import helper.Helper;

public class Armor extends Item {

    private int AC = 10;
    private boolean cursed = false;

    public Armor(int x, int y) {
        super("]", x, y);

        randomizeArmorType();
        randomizeCursed();
    }
    public Armor(Armor item) {
        super("]", item.getXPos(), item.getYPos());

        this.name = item.getName();
        this.cursed = item.getCursed();
        this.AC = item.getAC();
    }
    private void randomizeArmorType() {
        int type = Helper.random.nextInt(7) + 1; // TODO: Make this based on level or percentages

        switch (type) {
            case 1:
                name = "Leather Armor";
                AC = 8;
                break;
            case 2:
                name = "Ring Mail";
                AC = 7;
                break;
            case 3:
                name = "Studded Leather";
                AC = 7;
                break;
            case 4:
                name = "Scale Mail";
                AC = 6;
                break;
            case 5:
                name = "Chain Mail";
                AC = 5;
                break;
            case 6:
                name = "Splint Mail";
                AC = 4;
                break;
            case 7:
                name = "Banded Mail";
                AC = 4;
                break;
            case 8:
                name = "Plate Mail";
                AC = 3;
                break;
        }
    }
    private void randomizeCursed() {
        cursed = Helper.random.nextBoolean();
    }
    public boolean getCursed() {
        return cursed;
    }
    public int getAC() {
        return AC;
    }
}
