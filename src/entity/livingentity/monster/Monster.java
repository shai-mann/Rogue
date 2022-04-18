package entity.livingentity.monster;

import entity.Effect;
import entity.Entity;
import entity.Status;
import entity.lifelessentity.item.Item;
import entity.livingentity.Player;
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

    private Item stolenItem = null;
    private int stolenGold = 0;

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
        // TODO: drain max HP, drain XP (levels loss if go below thresh-hold)
        move();
        status.update();
        if (status.getHealth() <= 0) {
            this.die();
        }
    }

    // MONSTER MOVEMENT

    private void move() {
        if (stuck()) return;

        loopMovement(monsterAttr.movementAI());
        boolean shouldAttack = !monsterAttr.movementAI().blockAttackAITrigger();

        if (monsterAttr.movementAI().shouldTriggerSecondaryAI()) {
            loopMovement(monsterAttr.secondaryMovementAI());
            shouldAttack = shouldAttack && !monsterAttr.secondaryMovementAI().blockAttackAITrigger();
        }
        if (shouldAttack) {
            attack(); // TODO: switch to AttackAI when implemented
        }
    }

    /* MOVEMENT HELPERS */

    private void loopMovement(MovementAI ai) {
        for (int i = 0; i < monsterAttr.speed(); i++) {
            ai.move();
        }
    }

    /**
     * Any and all external logic that might prevent the monster from moving should be checked here
     * @return true if the monster cannot move, false otherwise
     */
    private boolean stuck() {
        return getStatus().getEffects().hasEffect(Effect.SUPPRESS_POWER);
    }

    // MONSTER ATTACK

    private void attack() {
        if (Helper.random.nextDouble() <= (double) GameManager.getPlayer().getStatus().getAc() / 10) {
            if (!getStatus().getEffects().hasEffect(Effect.SUPPRESS_POWER)) {
                switch (monsterAttr.attackAI().toLowerCase()) {
                    case "hit":
                        hitAttack();
                        break;
                    case "paralyze":
                    case "trap":
                        paralyzeAttack();
                        break;
                    case "confuse":
                        confuseAttack();
                        break;
                    case "intoxicate":
                        intoxicateAttack();
                        break;
                    case "weaken":
                        weakenAttack();
                        break;
                    case "steal_gold":
                        stealGoldAttack();
                        break;
                    case "steal_item":
                        stealItemAttack();
                        break;
                    case "rust":
                        rustAttack();
                        break;
                    case "xp_drain":
                        xpDrainAttack();
                        break;
                    case "hp_drain":
                        healthDrainAttack();
                        break;
                }
            } else {
                hitAttack();
            }
        } else {
            MessageBar.addMessage("The " + getName() + " misses");
        }
    }

    private void hitAttack() {
        if (monsterAttr.hitDamage() == 0) {
            MessageBar.addMessage("The " + getName() + " misses");
        } else if (Helper.random.nextDouble() <= monsterAttr.critChance()) { // critical
            if (Helper.random.nextDouble() <= 0.8) {
                GameManager.getPlayer().health -= Helper.random.nextInt(monsterAttr.critDamage()) + 1;
                MessageBar.addMessage("The " + getName() + " crits");
            } else {
                GameManager.getPlayer().health -= Helper.random.nextInt(monsterAttr.hitDamage()) + 1;
                GameManager.getPlayer().getStatus().setParalyzed(3);
            }
        } else {
            GameManager.getPlayer().health -= Helper.random.nextInt(monsterAttr.hitDamage()) + 1;
            MessageBar.addMessage("The " + getName() + " hits");
        }
    }

    private void paralyzeAttack() {
        Player player = GameManager.getPlayer();
        if (!player.getStatus().isParalyzed()) {
            GameManager.getPlayer().getStatus().setParalyzed(3);
        }
        MessageBar.addMessage("You can't move");
    }

    private void confuseAttack() {
        GameManager.getPlayer().getStatus().setConfused(Helper.random.nextInt(11) + 10);
        MessageBar.addMessage("You feel confused");
    }

    private void intoxicateAttack() {
        // TODO: delay between two steps
        GameManager.getPlayer().getStatus().setDrunk(3);
        MessageBar.addMessage("You feel strange");
    }

    private void weakenAttack() {
        if (GameManager.getPlayer().getStatus().getEffects().hasEffect(Effect.SUSTAIN_STRENGTH)) {
            MessageBar.addMessage("The " + getName() + "'s attack was magically deflected");
        } else {
            GameManager.getPlayer().getStatus().setWeakened(Helper.random.nextInt(10) + 2);
            MessageBar.addMessage("You feel weaker");
        }
    }

    private void stealGoldAttack() {
        Player p = GameManager.getPlayer();

        if (p.getGold() > 0 && Helper.random.nextInt(99) + 1 < 40) {
            stolenGold += p.stealGold(Helper.random.nextInt(50) + p.getGold() / 4);
            MessageBar.addMessage("Your pockets feel lighter");
            monsterAttr.setMovementAI("wander", this);
            // TODO: update this and stealItem attack to change to RUN movement type after stealing
        } else {
            MessageBar.addMessage("The " + getName() + " couldn't take your gold");
        }
    }

    private void stealItemAttack() {
        ArrayList<Item> items = (ArrayList<Item>) GameManager.getPlayer().getInventory().clone();

        if (stolenItem == null && items.size() != 0 && Helper.random.nextInt(99) + 1 > 70) {
            stolenItem = Helper.getRandom(items);
            GameManager.getPlayer().getInventory().remove(stolenItem);
            MessageBar.addMessage("Your backpack feels lighter");
            monsterAttr.setMovementAI("wander", this);
            monsterAttr.setAttackAI("hit", this);
        } else {
            MessageBar.addMessage("The " + getName() + " fails to steal from you");
        }
    }

    private void rustAttack() {
        if (GameManager.getPlayer().getWornItem() != null &&
                !GameManager.getPlayer().getStatus().getEffects().hasEffect(Effect.PROTECT_ARMOR)) {
            GameManager.getPlayer().getStatus().setAc(GameManager.getPlayer().getStatus().getAc() + 1);

            MessageBar.addMessage("Your armor feels worse");
        } else if (GameManager.getPlayer().getStatus().getEffects().hasEffect(Effect.PROTECT_ARMOR)) {
            MessageBar.addMessage("The " + getName() + "'s attack was magically deflected");
        }
    }

    private void xpDrainAttack() {
        GameManager.getPlayer().addExperience((int) -(GameManager.getPlayer().getExperience() * 0.9));
        MessageBar.addMessage("The " + name + " preys on your mind. Your experience drains away");
    }

    private void healthDrainAttack() {
        GameManager.getPlayer().drainMaxHealth((int) (GameManager.getPlayer().getMaxHealth() * 0.9));
        MessageBar.addMessage("");
    }

    // OVERRIDES

    @Override
    public boolean move(int direction) {
        super.move(direction);
        if (monsterAttr.isInvisible() && !GameManager.getPlayer().getStatus().getEffects().hasEffect(Effect.SEE_INVISIBLE)) {
            GameManager.add(overWrittenGraphic, getXPos(), getYPos());
        }
        return true;
    }

    // HELPERS
    
    private void die() {
        GameManager.add(overWrittenGraphic, getXPos(), getYPos());
        GameManager.getPlayer().addExperience(monsterAttr.experience());
        MessageBar.addMessage("You kill the " + name);

        if (Item.getItemAt(getXPos(), getYPos()) != null && Level.getLevel().getRoom(this) != null) {
            setLocation(Level.getLevel().getRoom(this).getRandomPointInBounds());
        }
        if (Helper.random.nextInt(99) + 1 < monsterAttr.treasureChance() || stolenItem != null) {
            Item.spawnItem(getXPos(), getYPos(), null, stolenItem);
        } else if (stolenGold > 0) {
            Item.spawnItem(getXPos(), getYPos(), Item.itemTypes.GOLD, null);
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
        for (int i = 0; i < monsters.size(); i++) {
            Monster monster = monsters.get(i);
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
                loadedMonsters.add(m);
            }
        }
        return loadedMonsters;
    }
}