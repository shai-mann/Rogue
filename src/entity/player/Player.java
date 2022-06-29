package entity.player;

import entity.component.Inventory;
import entity.component.Status;
import entity.lifeless.Staircase;
import entity.lifeless.item.Ring;
import entity.lifeless.item.combat.Armor;
import entity.lifeless.item.structure.Item;
import entity.player.trait.Trait;
import entity.player.trait.TraitLoader;
import entity.structure.AbstractLivingEntity;
import entity.structure.EntityProperties;
import entity.structure.LivingEntity;
import entity.util.MoveResult;
import main.GameManager;
import map.Map;
import map.level.Door;
import map.level.Level;
import map.level.Room;
import util.Helper;
import util.gamepanes.MessageBar;
import util.inventory.InventoryPane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static settings.Settings.*;

public class Player extends AbstractLivingEntity {

    private static final String PLAYER_NAME = "Player";
    private static final String PLAYER_GRAPHIC = "@";
    private static final Color PLAYER_GRAPHIC_COLOR = Color.YELLOW;
    private static final EntityProperties PLAYER_PROPERTIES = new EntityProperties(
            PLAYER_NAME, PLAYER_GRAPHIC, PLAYER_GRAPHIC_COLOR
    );
    private static final int PLAYER_STARTING_HEALTH = 20;

    private final Level level;

    private final PlayerStatsManager manager;

    private final Status status;
    private final PlayerKeyMap keyMap = new PlayerKeyMap();
    private final Inventory inventory = new Inventory();
    private boolean showInventory = false; // todo: switch such that inventory panel only has one keymapping to hide

    private Armor wornItem;
    private Item heldItem;
    private final List<Ring> rings = new ArrayList<>();

    private final List<Trait> traits = TraitLoader.traits();

    public Player(Point location, Level level) {
        super(PLAYER_PROPERTIES, location, PLAYER_STARTING_HEALTH);
        this.level = level;

        manager = new PlayerStatsManager(this);
        status = new Status(9);

        initKeyMap();
    }

    public void update() {
        if (shouldDie()) {
            die();
            return;
        }

        status.update();
    }

    /* SYSTEM METHODS */

    void levelUp() {
        changeMaxHealth((int) (maxHealth() * 0.1));
        // todo: increase maxDamage

        MessageBar.addMessage("Welcome to level " + level);
    }

    /* PLAYER ACTIONS */

    private void _move(Point displacement) {
        if (!canMove()) return;

        if (mustMoveRandom()) {
            List<Point> moves = Helper.getAdjacentPoints(new Point(0, 0), false);
            moves = moves.stream().filter((p -> level.isValidMove(this, p).isValidMove())).toList();
            attemptMove(Helper.getRandom(moves));
        }

        attemptMove(displacement);

        // todo: update game
        manager.tick(true);
        Level.getLevel().update();
    }

    private void rest() {
        manager.tickRegenerate();
        manager.tick(false);
    }

    private void search() {
        Room room = level.getRoom(this);
        if (room == null) return;

        for (Door d : room.doors().stream().filter(Door::isSecret).toList()) {
            if (Helper.isNextTo(location(), d.getLocation())) {
                d.reveal();
                MessageBar.addMessage("You discover a secret door");
            }
        }

        // todo: trap searching

        manager.tick(true);
        Map.getMap().update();
    }

    public void inventory() {
        new InventoryPane((JPanel) GameManager.getFrame().getContentPane());
        // todo: fix this system fr
    }

    private void useStaircase() {
        Optional<Staircase> staircase = level.getStaircases().stream().filter(
                (s -> s.location().equals(location()))
        ).findFirst();

        if (staircase.isEmpty()) return;

        Staircase.Direction direction = staircase.get().getDirection();

        level.newLevel(direction == Staircase.Direction.DOWN);
        moveTo(Level.getLevel().getStartingRoom().getRandomPointInBounds());
        Map.getMap().getStatusBar().updateStatusBar(); // todo: switch to general rendering update?

        manager.tick(true);
    }

    /* KEY INPUT HELPERS */

    private void hit(LivingEntity entity) {
//        double hitChance = (100 - ((10 - entity.getStatus().getAc()) * 3) + 30) / 100.0;
//        if (Helper.calculateChance(hitChance)) {
//            Status monsterStatus = monster.getStatus();
//            monsterStatus.lowerHealth(getDamage());
//            Map.getMap().update();
//            if (monster.getStatus().getHealth() > 0) {
//                MessageBar.addMessage("You hit the " + monster.getName());
//            }
//            if (monster.getStatus().isSleeping()) {
//                monster.getStatus().setSleeping(false); // TODO: move to part of monster class
//            }
//        }
    }

    @Override
    public void die() {

    } // todo: move method location (not organized right)

    /**
     * This is the version of {@code _move()} which has already taken into account the Player's {@link Status}
     * effects.
     * @param displacement the displacement to move the Player by.
     */
    private void attemptMove(Point displacement) {
        MoveResult moveResult = level.isValidMove(this, displacement);
        if (!moveResult.takesAction()) return;

        if (moveResult.hitsEntity()) {
            hit(moveResult.getCollisionEntity());
            return;
        }

        // if neither of those, must be a valid movement
        super.move(displacement);
    }

    private boolean canMove() {
        return !status.is(Status.Stat.PARALYZED) && !status.is(Status.Stat.SLEEPING);
    }

    private boolean mustMoveRandom() {
        return status.is(Status.Stat.CONFUSED) && status.is(Status.Stat.DRUNK);
    }

    /* KEY HANDLING */

    private void initKeyMap() {
        keyMap.addKeyListener(MOVE_UP, (e -> _move(new Point(0, -1))));
        keyMap.addKeyListener(MOVE_DOWN, (e -> _move(new Point(0, 1))));
        keyMap.addKeyListener(MOVE_LEFT, (e -> _move(new Point(-1, 0))));
        keyMap.addKeyListener(MOVE_RIGHT, (e -> _move(new Point(1, 0))));

        keyMap.addKeyListener(REST, (e -> rest()));
        keyMap.addKeyListener(SEARCH, (e -> search()));
        keyMap.addKeyListener(INVENTORY, (e -> inventory()));
        keyMap.addKeyListener(USE_STAIRCASE, (e -> useStaircase()));
    }

    public void apply(JPanel panel) {
        keyMap.apply(panel);
    }

    /* GETTERS/SETTERS */

    public int getLevel() {
        return manager.level();
    }

    public void artificiallyLevelUp() {
        manager.artificialLevelUp();
    }

    public int getExperience() {
        return manager.getExperience();
    }
    public void changeExperience(int delta) {
        manager.changeExperience(delta);
    }
    public int getLevelingThreshold() {
        return manager.levelingThreshold();
    }

    public String getHungerLevel() {
        return manager.getHungerLevel();
    }

    public int getGold() {
        return inventory.getGold();
    }
    public int changeGold(int delta) {
        return inventory.changeGold(delta);
    }

    public Status getStatus() {
        return status;
    }

    public Armor getWornItem() {
        return wornItem;
    }
    public void setWornItem(Armor wornItem) {
        this.wornItem = wornItem;
    }

    public Item getHeldItem() {
        return heldItem;
    }
    public void setHeldItem(Item heldItem) {
        this.heldItem = heldItem;
    }

    public List<Item> getInventory() {
        return inventory.getItems();
    }

    public boolean isWorn(Ring ring) {
        return rings.contains(ring);
    }
    public boolean wear(Ring ring) {
        if (rings.size() < 2) {
            rings.add(ring);
            return true;
        }

        return false;
    }
    public void remove(Ring ring) {
        rings.remove(ring);
    }

    public void eat() {
        manager.resetHunger();
        Map.getMap().getStatusBar().updateStatusBar();
    }
}
