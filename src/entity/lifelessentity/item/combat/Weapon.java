package entity.lifelessentity.item.combat;

import entity.lifelessentity.item.Item;
import helper.Helper;
import main.GameManager;
import util.MessageBar;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

public class Weapon extends Item {

    private int damage = 1;
    private boolean throwable = false;
    private boolean isBow = false;
    private boolean cursed = false;
    private int throwDamage;
    private int amount = 1;

    public Weapon(String dataFilePath, int x, int y) {
        super(")", x, y);
        if (!(dataFilePath == null)) {
            loadDataFile(dataFilePath);
        } else {
            try {
                File file = (File) Helper.getRandom(new ArrayList(Arrays.asList(
                        new File(new File(
                                Weapon.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent()
                                + "/data/weapons").listFiles()
                )));
//                File file = null; //TODO: when exporting game to JAR, delete this line and correct section below
//
//                if (file == null) {
//                    file = (File) Helper.getRandom(new ArrayList(Arrays.asList(
//                            new File("./resources/data/weapons").listFiles()
//                    )));
//                } else {
//                    file = (File) Helper.getRandom(new ArrayList(Arrays.asList(
//                            new File(new File(
//                                    Weapon.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent()
//                                    + "/data/weapons").listFiles()
//                    )));
//                }
                loadDataFile(file.getPath());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        if (damage <= 1) {
            damage = 2;
        }
    }
    public Weapon(Weapon weapon) {
        super(")", GameManager.getPlayer().getXPos(), GameManager.getPlayer().getYPos());
        overWrittenGraphic = "-";
        name = weapon.getName();
        damage = weapon.getMaxDamage();
        throwable = weapon.isThrowable();
        isBow = weapon.isBow();
        cursed = weapon.isCursed();
    }

    // DATA LOADING

    private void loadDataFile(String path) {
        String line;
        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                applyAttributeFromLine(line);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: file not found");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
    }
    private void applyAttributeFromLine(String line) {
        String[] parsed = line.split(":");
        switch (parsed[0].toLowerCase()) {
            case "damage":
                this.damage = parseDiceNotation(parsed[1]);
                break;
            case "throwable":
                this.throwable = Boolean.valueOf(parsed[1]);
                break;
            case "isbow":
                this.isBow = Boolean.valueOf(parsed[1]);
                break;
            case "name":
                this.name = parsed[1];
                break;
            case "curseChance":
                this.cursed = Helper.random.nextInt(99) + 1 <= Integer.valueOf(parsed[1]);
                break;
            case "throwdamage":
                this.throwDamage = parseDiceNotation(parsed[1]);
                break;
            case "amount":
                this.amount = parseDiceNotation(parsed[1]);
                if (amount > 1) {
                    name = name.concat("s");
                }
                break;
        }
    }

    private static int parseDiceNotation(String die) {
        String[] parts = die.split("d");
        return Integer.valueOf(parts[0]) * Integer.valueOf(parts[1]);
    }

    // OVERRIDES

    @Override
    public void use() {
        if (GameManager.getPlayer().getHeldItem() != null && GameManager.getPlayer().getHeldItem().equals(this)) {
            GameManager.getPlayer().setHeldItem(null);
            MessageBar.addMessage("You put away the " + getName());
        }
        GameManager.getPlayer().setHeldItem(this);
        MessageBar.addMessage("You hold the " + getName());
    }
    @Override
    public String getName() {
        return isHeld() ? super.getName().concat(" (held)") : super.getName();
    }

    // GETTERS/SETTERS

    private int getMaxDamage() {
        return damage;
    }
    public void enchant() {
        damage++;
    }
    public boolean isCursed() {
        return cursed;
    }
    public int getDamage() {
        return Helper.random.nextInt(damage - 1) + 1;
    }
    public int getThrowDamage() {
        return Helper.random.nextInt(throwDamage) + 1;
    }
    public boolean isBow() {
        return isBow;
    }
    public boolean isThrowable() {
        return throwable;
    }
    public boolean isHeld() {
        return this.equals(GameManager.getPlayer().getHeldItem());
    }
}
