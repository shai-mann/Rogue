package entity.livingentity.monster;

import util.DiceRange;

import java.util.List;

/**
 * A record that contains much of the information stored regarding monsters in external files. Used
 * when reading in the files to store the class types of monsters in the game.
 *
 * AI types are stored as strings so that when the class is given to a monster, the monster can
 * construct their own instance of the {@link entity.livingentity.monster.ai.MovementAI} or
 * {@link entity.livingentity.monster.ai.AttackAI}.
 */
public record MonsterClass(
        String name,
        String graphic,
        double critChance,
        DiceRange hitDamage,
        DiceRange critDamage,
        int speed,
        int range,
        DiceRange health,
        String movementAI,
        String secondaryMovementAI,
        String attackAI,
        boolean invisible,
        int defaultAC,
        int experience, // TODO: range?
        double treasureChance,
        List<Integer> spawnableLevels
        ) {

        // override certain getters to force deep copies
        @Override
        public DiceRange hitDamage() {
                return hitDamage.scale(1);
        }

        @Override
        public DiceRange critDamage() {
                return critDamage.scale(1);
        }

        @Override
        public DiceRange health() {
                return health.scale(1);
        }
}
