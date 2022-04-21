package entity.livingentity.monster;

import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Handles much of the file loading relating to Monsters and their variations.
 *
 * Monster loading happens in two main places during the game's life:
 *  1) At the program's initialization, the available Monster resource files are loaded
 *      and processed.
*   2) Every time the level changes - so that the monster's valid level values can be adjusted.
 */
public class MonsterLoader {

    private static String FILE_PATH;

    static {
        try {
            FILE_PATH = new File(
                    Objects.requireNonNull(
                            MonsterLoader.class.getClassLoader().getResource("./data/monsters")
                    ).toURI()
            ).getPath();
        } catch (URISyntaxException e) {
            new Exception("Failed to locate monster files").printStackTrace();
            e.printStackTrace();
        }
    }

    public static List<MonsterClass> monsterClasses;

    public static void loadMonsters() {
        monsterClasses = loadMonsterClasses(Objects.requireNonNull(new File(FILE_PATH).listFiles()));
    }

    public static List<MonsterClass> getSpawnableMonsterClasses(int level) {
        return monsterClasses.stream().filter((clazz) -> clazz.spawnableLevels().contains(level)).toList();
    }

    private static List<MonsterClass> loadMonsterClasses(File[] files) {
        List<MonsterClass> monsterClasses = new ArrayList<>();

        for (File f : files) {
            MonsterClassBuilder builder = new MonsterClassBuilder();

            try {
                FileReader fileReader = new FileReader(f);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    readAttribute(builder, line);
                }

                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            monsterClasses.add(builder.build());
        }

        return monsterClasses;
    }

    private static void readAttribute(MonsterClassBuilder builder, String line) {
        String[] attributePair = line.split(":");
        builder.setAttribute(attributePair[0].toLowerCase(), attributePair[1]);
    }
}
