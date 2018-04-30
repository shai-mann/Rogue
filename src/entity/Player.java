package entity;

<<<<<<< HEAD
=======
import entity.item.Armor;
import entity.item.Gold;
import entity.item.Item;
import entity.item.Ring;
import entity.monster.Monster;
>>>>>>> items
import extra.GravePane;
import extra.inventory.InventoryPane;
import extra.MessageBar;
import helper.Helper;
import main.GameManager;
import map.Map;
import map.level.Room;

<<<<<<< HEAD
=======
import javax.swing.*;
>>>>>>> items
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class Player extends Entity implements KeyListener {

    private Map map;
    private Status status;
    private boolean showInventory = false;
    private Container savedContentPane;

<<<<<<< HEAD
    // TODO: Convert to Helper.random on merge

    public Player(Room room) {
        super("@", 0, 0);
        setLocation(room);
=======
    private Armor wornItem;
    private Item heldItem;
    private Ring leftRing;
    private Ring rightRing;

    private int gold = 0;

    private ArrayList<Item> inventory = new ArrayList<>();

    public Player() { // TODO: make Player spawn in non-arbitrary location
        super("@", 1, 1);
>>>>>>> items
        GameManager.getFrame().addKeyListener(this);
        status = new Status();
        status.setAc(8);
        map = Map.getMap();
    }
    public void keyPressed(KeyEvent e) {
        if (checkDeath()) {
            return;
        }
        boolean moved = false;
        if (!status.isParalyzed() && !status.isConfused()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                case KeyEvent.VK_UP:
                    moved = move(UP);
                    break;
                case KeyEvent.VK_S:
                case KeyEvent.VK_DOWN:
                    moved = move(DOWN);
                    break;
                case KeyEvent.VK_A:
                case KeyEvent.VK_LEFT:
                    moved = move(LEFT);
                    break;
                case KeyEvent.VK_D:
                case KeyEvent.VK_RIGHT:
                    moved = move(RIGHT);
                    break;
                default:
                    break;
            }
            if (e.getKeyCode() == KeyEvent.VK_I) {
                showInventory = !showInventory;
                if (showInventory) {
                    savedContentPane = GameManager.getFrame().getContentPane();
                    new InventoryPane();
                } else {
                    GameManager.replaceContentPane((JPanel) savedContentPane);
                }
            }
        }
        if (status.isConfused() || status.isDrunk()) {
            int[] directions = {UP,DOWN,RIGHT,LEFT};
            move(directions[new Random().nextInt(directions.length)]);
        }
        if (moved) {
            map.update();
        } else {
            for (Monster monster: Monster.getMonsters()) {
                if (isNextTo(monster)) {
                    double hitChance = (100 - ((10 - monster.getStatus().getAc()) * 3) + 30) / 100;
                    if (Helper.random.nextDouble() <= hitChance) {
                        Status monsterStatus = monster.getStatus();
                        monsterStatus.setHealth(monsterStatus.getHealth() - 1); // TODO: base this on sword
                        map.update();
                        MessageBar.addMessage("You hit the " + monster.getName());
                    }
                }
            }
        }
        status.update();
    }
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
    private void setLocation(Room room) {
        // TODO: Change this to be Helper.random
        super.setLocation(room.getRandomPointInBounds());
    }
    public int getHealth() {
        return health;
    }
    public int getGold() {
        return gold;
    }
    public void update() {
        if (checkDeath()) {
            new GravePane();
        }
        checkToPickUpItem();
    }
    private void checkToPickUpItem() {
        if (overWrittenGraphic.equals("*")) {
            Gold foundGold = (Gold) Item.getItemAt(getXPos(), getYPos());

            overWrittenGraphic = foundGold.overWrittenGraphic;
            gold += foundGold.getAmount();

            MessageBar.addMessage("You picked up " + foundGold.getAmount() + " gold");
            Item.items.remove(foundGold);
        } else if (overWrittenGraphic.equals("]")) {
            Item foundArmor = Item.getItemAt(getXPos(), getYPos());

            overWrittenGraphic = foundArmor.overWrittenGraphic;

            inventory.add(foundArmor);
            MessageBar.addMessage("You picked up " + foundArmor.getName());
            Item.items.remove(foundArmor);
        }
    }
    private boolean checkDeath() {
        return getHealth() <= 0;
    }
    public Status getStatus() {
        return status;
    }
    public ArrayList<Item> getInventory() {
        return inventory;
    }
}
