package entity.livingentity.monster;

import util.DiceRange;

import java.util.*;
import java.util.function.Consumer;

/**
 * A simple builder class for {@link MonsterClass}.
 */
public class MonsterClassBuilder {

    private final Map<String, Consumer<String>> attributesMap = new HashMap<>() {{
        put("name", (s) -> name = s);
        put("graphic", (s) -> graphic = s);
        put("critchance", (s) -> {
            critChance = Double.parseDouble(s);
            hasSetCritDamage = true;
        });
        put("hitdamage", (s) -> {
            hitDamage = new DiceRange(s);
            if (!hasSetCritDamage) {
                attributesMap.get("critdamage").accept(hitDamage.scale(2).toString());
            }
        });
        put("critdamage", (s) -> critDamage = new DiceRange(s));
        put("speed", (s) -> speed = Integer.parseInt(s));
        put("range", (s) -> range = Integer.parseInt(s));
        put("health", (s) -> health = new DiceRange(s));
        put("movementtype", (s) -> movementAI = s);
        put("secondarymovementtype", (s) -> secondaryMovementAI = s);
        put("attacktype", (s) -> attackAI = s);
        put("invisible", (s) -> invisible = Boolean.parseBoolean(s));
        put("ac", (s) -> defaultAC = Integer.parseInt(s));
        put("experience", (s) -> experience = Integer.parseInt(s));
        put("treasure", (s) -> treasureChance = Integer.parseInt(s) / 100.0);
        put("level", (s) -> spawnableLevels = readSpawnableLevels(s));
    }};

    private String name = "[DEFAULT NAME]";
    private String graphic = "<INVALID GRAPHIC>";
    private double critChance = 0.05;
    private DiceRange hitDamage = new DiceRange("1d1");
    private DiceRange critDamage = new DiceRange("2d1");
    private int speed = 1;
    private int range = 10;
    private DiceRange health = new DiceRange("20d1");
    private String movementAI = "track";
    private String secondaryMovementAI = "wander";
    private String attackAI = "hit";
    private boolean invisible = false;
    private int defaultAC = 0;
    private int experience = 0; // TODO: range?
    private double treasureChance = 0;
    private List<Integer> spawnableLevels = Collections.singletonList(-1);

    // in the instance where hitDamage is set but critDamage hasn't been, critDamage = 2 * hitDamage
    private boolean hasSetCritDamage = false;

    public MonsterClassBuilder() {}

    public MonsterClassBuilder setAttribute(String attribute, String value) {
        try {
            // trims just in case bad extra spacing
            attributesMap.get(attribute).accept(value.trim());
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        return this;
    }

    public MonsterClass build() {
        hasSetCritDamage = false;
        return new MonsterClass(
                name, graphic, critChance, hitDamage, critDamage,
                speed, range, health, movementAI, secondaryMovementAI,
                attackAI, invisible, defaultAC, experience, treasureChance,
                spawnableLevels
        );
    }

    /* DATA PARSING HELPERS */

    private List<Integer> readSpawnableLevels(String s) {
        List<Integer> validLevels = new ArrayList<>();
        String[] numbers = s.split(",");
        for (String string : numbers) {
            if (string.contains("-")) {
                for (int i = Integer.parseInt(string.split("-")[0].trim());
                     i <= Integer.parseInt(string.split("-")[1].trim()); i++) {
                    validLevels.add(i);
                }
            } else {
                validLevels.add(Integer.parseInt(string.trim()));
            }
        }

        return validLevels;
    }
}
