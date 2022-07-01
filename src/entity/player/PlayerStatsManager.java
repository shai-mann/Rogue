package entity.player;

import entity.component.Effect;

import java.util.List;

/**
 * The PlayerStatsManager handles the experience system, the hunger system, and the regeneration system
 * for the {@link Player}. It is ticked by the player, and mutates the player as it ticks.
 */
public class PlayerStatsManager {

    private enum HungerLevel {
        SATED("Sated", 0),
        HUNGRY("Hungry", 200),
        STARVING("Starving", 400),
        WEAK("Weak", 800),
        DEAD("Dead", 1600);

        private final String name;
        private final int threshold;

        HungerLevel(String name, int threshold) {
            this.name = name;
            this.threshold = threshold;
        }
    }

    private static final List<HungerLevel> hungerLevels = List.of(
            HungerLevel.SATED, HungerLevel.HUNGRY, HungerLevel.STARVING, HungerLevel.WEAK, HungerLevel.DEAD
    );
    private static final int[] levelingThresholds = {0, 10, 20, 40, 80, 160, 320, 640, 1280, 2560, 5120, 10240, 20480};
    private static final int REGEN_STEPS = 21;

    private int experience = 0;
    private int level = 1;

    private double stepsTakenSinceMeal = 0;
    private HungerLevel hungerLevel = HungerLevel.SATED;
    private int regenStepsCounter = 0;

    private final Player player;

    public PlayerStatsManager(Player player) {
        this.player = player;
    }

    public void tick() {
        if (shouldLevelUp()) levelUp();
    }

    // todo: remove
    public void tick(boolean tickHunger) {
        if (shouldLevelUp()) levelUp();
        if (tickHunger) tickHunger();
        tickRegenerate();
    }

    /* EXPERIENCE SYSTEM */

    public void changeExperience(int delta) {
        experience += delta;
    }

    public void artificialLevelUp() {
        experience = levelingThreshold() + 1;
    }

    private boolean shouldLevelUp() {
        return experience >= levelingThreshold();
    }

    private void levelUp() {
        level++;

        player.levelUp();

        if (shouldLevelUp()) levelUp();
    }

    /* HUNGER SYSTEM */

    public void tickHunger() {
        stepsTakenSinceMeal++;

        if (player.getStatus().hasEffect(Effect.Type.SLOW_DIGESTION)) stepsTakenSinceMeal -= 0.5;

        HungerLevel nextLevel = getNextHungerLevel();

        if ((int) stepsTakenSinceMeal >= nextLevel.threshold) {
            hungerLevel = nextLevel;
        }

        if (hungerLevel.equals(HungerLevel.DEAD)) player.die();
    }

    private HungerLevel getNextHungerLevel() {
        return hungerLevels.get(hungerLevels.indexOf(hungerLevel) + 1);
    }

    public void resetHunger() {
        stepsTakenSinceMeal = 0;
    }

    /* REGENERATION SYSTEM */

    public void tickRegenerate() {
        if (player.health() >= player.maxHealth()) return;

        regenStepsCounter++;
        if (player.getStatus().hasEffect(Effect.Type.REGENERATION))

        if (shouldRegenerate()) {
            player.changeHealth(1);
            regenStepsCounter = 0;
        }
    }

    private boolean shouldRegenerate() {
        return regenStepsCounter >= REGEN_STEPS - level;
    }

    /* GETTERS */

    public int getExperience() {
        return experience;
    }

    public int level() {
        return level;
    }

    public int levelingThreshold() {
        return levelingThresholds[level];
    }

    public String getHungerLevel() {
        return hungerLevel.name;
    }

}
