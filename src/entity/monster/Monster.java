package entity.monster;

import entity.component.Effect;
import entity.component.Inventory;
import entity.component.Status;
import entity.monster.ai.MovementAI;
import entity.structure.AbstractLivingEntity;
import entity.structure.EntityProperties;
import entity.structure.LivingEntity;
import entity.util.MoveResult;
import map.level.Level;
import map.level.Room;
import map.level.table.GameTable;
import util.Helper;
import rendering.panels.gamepanes.MessageBar;

import java.awt.*;
import java.util.Optional;

public class Monster extends AbstractLivingEntity implements LivingEntity {

    public static int DEFAULT_HEALTH = 20;

    private MonsterAttributes monsterAttr;
    private final Status status;
    public final Inventory inventory = new Inventory();

    public Monster(MonsterClass monsterClass, Point location) {
        super(
                new EntityProperties(monsterClass.name(), monsterClass.graphic(), Color.RED),
                location, DEFAULT_HEALTH
        );

        status = new Status(monsterAttr.defaultAC());
        monsterAttr = new MonsterAttributes(monsterClass, this);
    }

    // MONSTER BEHAVIOR

    public void update() {
        move();

        status.update();
        if (health() <= 0) {
            die();
        }
    }

    // MONSTER MOVEMENT

    private void move() {
        if (stuck()) return;
        MoveResult result = loopMovement(monsterAttr.movementAI());

        if (monsterAttr.movementAI().blockAttackAITrigger()) return;
        if (result.hitsEntity()) monsterAttr.attackAI().attack(); // todo: pass entity?
    }

    /* MOVEMENT HELPERS */

    private MoveResult loopMovement(MovementAI ai) {
        boolean hasMoved = false;
        for (int i = 0; i < monsterAttr.speed(); i++) {
            MoveResult result = ai.move();

            if (result.isValidMove()) hasMoved = true;
            if (result.hitsEntity()) return result;
        }

        return new MoveResult(hasMoved);
    }

    /**
     * Any and all external logic that might prevent the monster from moving should be checked here
     * @return true if the monster cannot move, false otherwise
     */
    private boolean stuck() {
        return getStatus().hasEffect(Effect.Type.SUPPRESS_POWER);
    }

    private boolean attackBlocked() {
        return false;
    }

    // OVERRIDES

    @Override
    public void render(GameTable table) {
        if (monsterAttr.isInvisible()) return;
        if (!Level.getLevel().getPlayer().getStatus().hasEffect(Effect.Type.SEE_INVISIBLE)) return;

        super.render(table);
    }

    @Override
    public void die() {
        Level.getLevel().getPlayer().changeExperience(monsterAttr.experience());
        MessageBar.addMessage("You kill the " + name());

        // todo: loot tables for monsters
//        if (Item.getItemAt(getXPos(), getYPos()) != null && Level.getLevel().getRoom(this) != null) {
//            setLocation(Level.getLevel().getRoom(this).getRandomPointInBounds());
//        }
//        if (Helper.random.nextInt(99) + 1 < monsterAttr.treasureChance() || inventory.hasItems()) {
//            Item.spawnItem(getXPos(), getYPos(), null, inventory.getItems().get(0));
//        } else if (inventory.getGold() > 0) {
//            Item.spawnItem(getXPos(), getYPos(), Item.itemTypes.GOLD, null); // TODO: not right amount of gold??
//        } else if (Helper.random.nextInt(2) == 1){
//            Item.spawnItem(getXPos(), getYPos(), Item.itemTypes.FOOD, null);
//            MessageBar.addMessage("The " + name + " drops some food");
//        } else {
//            Item.spawnItem(getXPos(), getYPos(), Item.itemTypes.GOLD, null);
//            MessageBar.addMessage("The " + name + " drops some gold");
//        }
    }

    @Override
    public String graphic() {
        Optional<String> graphicOverride = monsterAttr.movementAI().hasOverriddenGraphic();
        return graphicOverride.orElseGet(super::graphic);
    }

    // HELPERS

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

    // todo: move to level or LevelGenerator
    public static void createMonster(Room room, int level) {
        new Monster(Helper.getRandom(MonsterLoader.getSpawnableMonsterClasses(level)), room.getRandomPointInBounds());
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
}