package entity.livingentity.monster;

import entity.Entity;
import entity.component.Effect;
import entity.component.Inventory;
import entity.component.Status;
import entity.lifelessentity.item.Item;
import entity.livingentity.monster.ai.MovementAI;
import main.GameManager;
import map.level.Level;
import map.level.Passageway;
import map.level.Room;
import util.Helper;
import util.gamepanes.MessageBar;

import java.awt.*;
import java.util.ArrayList;

public class Monster extends Entity {

    private static ArrayList<Monster> monsters = new ArrayList<>();

    public static int DEFAULT_HEALTH = 20;

    private MonsterAttributes monsterAttr;
    private final Status status;
    public final Inventory inventory = new Inventory();

    public Monster(MonsterClass monsterClass, int x, int y) {
        super(monsterClass.graphic(), x, y);
        monsters.add(this);

        status = new Status(this);
        monsterAttr = new MonsterAttributes(monsterClass, this);
        super.name = monsterClass.name();
        status.setAc(monsterAttr.defaultAC());
    }

    // MONSTER BEHAVIOR

    private void runUpdate() {
        move();
        status.update();
        if (status.getHealth() <= 0) {
            this.die();
        }
    }

    // MONSTER MOVEMENT

    private void move() {
        if (stuck()) return;

        boolean shouldAttack = !loopMovement(monsterAttr.movementAI())
                && !monsterAttr.movementAI().blockAttackAITrigger();

        if (shouldAttack && !attackBlocked()) {
            monsterAttr.attackAI().attack();
        }
    }

    /* MOVEMENT HELPERS */

    private boolean loopMovement(MovementAI ai) {
        boolean hasMoved = false;
        for (int i = 0; i < monsterAttr.speed(); i++) {
            hasMoved = ai.move() || hasMoved;
        }

        return hasMoved;
    }

    /**
     * Any and all external logic that might prevent the monster from moving should be checked here
     * @return true if the monster cannot move, false otherwise
     */
    private boolean stuck() {
        return getStatus().getEffects().hasEffect(Effect.SUPPRESS_POWER);
    }

    private boolean attackBlocked() {
        return false;
    }

    // OVERRIDES

    @Override
    public boolean move(int direction) {
        boolean out = super.move(direction);
        if (monsterAttr.isInvisible() && !GameManager.getPlayer().getStatus().getEffects().hasEffect(Effect.SEE_INVISIBLE)) {
            GameManager.add(overWrittenGraphic, getXPos(), getYPos());
        } // TODO: bad pattern
        return out; // TODO: return actual value?
    }

    // HELPERS
    
    private void die() {
        GameManager.add(overWrittenGraphic, getXPos(), getYPos());
        GameManager.getPlayer().addExperience(monsterAttr.experience());
        MessageBar.addMessage("You kill the " + name);

        if (Item.getItemAt(getXPos(), getYPos()) != null && Level.getLevel().getRoom(this) != null) {
            setLocation(Level.getLevel().getRoom(this).getRandomPointInBounds());
        }
        if (Helper.random.nextInt(99) + 1 < monsterAttr.treasureChance() || inventory.hasItems()) {
            Item.spawnItem(getXPos(), getYPos(), null, inventory.getItems().get(0));
        } else if (inventory.getGold() > 0) {
            Item.spawnItem(getXPos(), getYPos(), Item.itemTypes.GOLD, null); // TODO: not right amount of gold??
        } else if (Helper.random.nextInt(2) == 1){
            Item.spawnItem(getXPos(), getYPos(), Item.itemTypes.FOOD, null);
            MessageBar.addMessage("The " + name + " drops some food");
        } else {
            Item.spawnItem(getXPos(), getYPos(), Item.itemTypes.GOLD, null);
            MessageBar.addMessage("The " + name + " drops some gold");
        }
        monsters.remove(this);
    }

    public void polymorph(MonsterClass monsterClass) {
        // TODO: make not change health and certain other attributes?
        monsterAttr = new MonsterAttributes(monsterClass, this);
    }

    // GETTERS

    public Status getStatus() {
        return status;
    }
    public MonsterAttributes attributes() {
        return monsterAttr;
    }

    // MONSTER SPAWNING METHODS

    public static void createMonster(Room room, int level) {
        Point location = room.getRandomPointInBounds();
        new Monster(Helper.getRandom(MonsterLoader.getSpawnableMonsterClasses(level)), location.x, location.y);
    }
    public static void spawnMonsters(int level) {
        for (Room room : Room.rooms) {
            if (!room.equals(Level.getLevel().getStartingRoom())) {
                // 55% chance of 1st monster
                if (Helper.random.nextInt(99) + 1 >= 45) {
                    createMonster(room, level);
                    // 25% chance of 2nd monster
                    if (Helper.random.nextInt(99) + 1 >= 75) {
                        createMonster(room, level);
                        // 2% chance of 3rd monster
                        if (Helper.random.nextInt(99) + 1 >= 98) {
                            createMonster(room, level);
                        }
                    }
                }
            }
        }
    }

    // STATIC METHODS

    public static void update() {
        for (Monster monster : monsters) {
            monster.runUpdate();
        }
    }
    public static Monster getClosestMonster(Entity e) {
        Monster closest;
        if (monsters.size() != 0) {
            closest = getMonsters().get(0);
        } else {
            return null;
        }
        for (Monster m : getMonsters()) {
            if (Passageway.getDistance(m.getLocation(), e.getLocation()) < Passageway.getDistance(closest.getLocation(), e.getLocation())) {
                closest = m;
            }
        }
        return closest;
    }
    public static ArrayList<Monster> getMonsters() {
        return monsters;
    }
    public static void setMonsters(ArrayList<Monster> ms) {
        monsters = ms;
    }
    public static ArrayList<Monster> getLoadedMonsters() {
        ArrayList<Monster> loadedMonsters = new ArrayList<>();
        for (Monster m : monsters) {
            if (Level.getLevel().getShownPoints().contains(m.getLocation())) {
                loadedMonsters.add(m); // todo: adjust to new rendering/loading system
            }
        }
        return loadedMonsters;
    }
}