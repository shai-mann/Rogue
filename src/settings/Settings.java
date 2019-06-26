package settings;

import javafx.util.Pair;
import util.SettingsPane;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.function.BinaryOperator;

public class Settings {

    /*
     * Text Size Settings:
     * small/default is 12
     * normal is 24
     * large is 36
     */

    public static int TEXT_SIZE = 12;
    static int TEXT_RESIZER = 1;

    public static int getTextSize() {
        return TEXT_SIZE * TEXT_RESIZER;
    }
    public static void setTextSize(int size) {
        if (size > 3) {
            throw new IllegalArgumentException("Size too large");
        }
        TEXT_RESIZER = size;
    }

    /*
     * Key binds:
     *      There are 8 keys, and there cannot be two actions with the same key.
     */

    private enum keyBind {
        MOVE_DOWN,
        MOVE_UP,
        MOVE_RIGHT,
        MOVE_LEFT,
        SEARCH,
        USE_STAIRCASE,
        INVENTORY,
        REST
    }

    // Default key values:

    public static final int DOWN_DEFAULT = KeyEvent.VK_DOWN;
    public static final int UP_DEFAULT = KeyEvent.VK_UP;
    public static final int RIGHT_DEFAULT = KeyEvent.VK_RIGHT;
    public static final int LEFT_DEFAULT = KeyEvent.VK_LEFT;

    public static final int SEARCH_DEFAULT = KeyEvent.VK_F;
    public static final int USE_STAIRCASE_DEFUALT = KeyEvent.VK_ENTER;
    public static final int INVENTORY_DEFUALT = KeyEvent.VK_I;
    public static final int REST_DEFAULT = KeyEvent.VK_R;

    // dynamic key values - TODO: move defaults from here to a file so that the reset defaults will read from the file

    public static Integer MOVE_DOWN = KeyEvent.VK_DOWN;
    public static Integer MOVE_UP = KeyEvent.VK_UP;
    public static Integer MOVE_RIGHT = KeyEvent.VK_RIGHT;
    public static Integer MOVE_LEFT = KeyEvent.VK_LEFT;

    public static Integer SEARCH = KeyEvent.VK_F;
    public static Integer USE_STAIRCASE = KeyEvent.VK_ENTER;
    public static Integer INVENTORY = KeyEvent.VK_I;
    public static Integer REST = KeyEvent.VK_R;

    public static ArrayList<Integer> getKeys() {
        ArrayList<Integer> bindings = new ArrayList<>();
        bindings.add(MOVE_DOWN);
        bindings.add(MOVE_UP);
        bindings.add(MOVE_RIGHT);
        bindings.add(MOVE_LEFT);
        bindings.add(SEARCH);
        bindings.add(USE_STAIRCASE);
        bindings.add(INVENTORY);
        bindings.add(REST);

        return bindings;
    }

    public static void setKeyBind(int keyBind, int keyValue) {
        Settings.keyBind k;
        switch (keyBind) {
            case 0:
                k = Settings.keyBind.MOVE_DOWN;
                break;
            case 1:
                k = Settings.keyBind.MOVE_UP;
                break;
            case 2:
                k = Settings.keyBind.MOVE_RIGHT;
                break;
            case 3:
                k = Settings.keyBind.MOVE_LEFT;
                break;
            case 4:
                k = Settings.keyBind.SEARCH;
                break;
            case 5:
                k = Settings.keyBind.USE_STAIRCASE;
                break;
            case 6:
                k = Settings.keyBind.INVENTORY;
                break;
            default:
                k = Settings.keyBind.REST;
                break;
        }
        setKeyBind(k, keyValue);
    }
    private static void setKeyBind(keyBind type, int keyValue) {
        switch (type) {
            case MOVE_DOWN:
                MOVE_DOWN = keyValue;
                break;
            case MOVE_UP:
                MOVE_UP = keyValue;
                break;
            case MOVE_RIGHT:
                MOVE_RIGHT = keyValue;
                break;
            case MOVE_LEFT:
                MOVE_LEFT = keyValue;
                break;
            case SEARCH:
                SEARCH = keyValue;
                break;
            case USE_STAIRCASE:
                USE_STAIRCASE = keyValue;
                break;
            case INVENTORY:
                INVENTORY = keyValue;
                break;
            case REST:
                REST = keyValue;
                break;
        }
    }
    public static String keyCodeToString(Integer keyEvent) {
        int kv = keyEvent;
        if (kv == MOVE_DOWN) return "Down";
        if (kv == MOVE_UP) return "Up";
        if (kv == MOVE_RIGHT) return "Right";
        if (kv == MOVE_LEFT) return "Left";
        if (kv == INVENTORY) return "Inventory";
        if (kv == USE_STAIRCASE) return "Staircase";
        if (kv == SEARCH) return "Find";
        if (kv == REST) return "Rest";
        else return "<Invalid Key Code>";
    }
    public static boolean checkValidKey(int key) {
        ArrayList<Pair> pairs = SettingsPane.getActionQueue();
        ArrayList<Integer> keys = getKeys();
        for (Pair p : pairs) {
            keys.set((Integer) p.getKey(), (Integer) p.getValue());
        }
        for (Integer i : keys) {
            if (i == key) {
                return false;
            }
        }
        return true;
    }

    /*
    * This code is for when I create the segment involving changing the symbols that represent parts of the game.
      try {
            File[] files = new File(new File(
                    Monster.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent()
                    + "/data/monsters").listFiles();

            for (File f : files) {
                ArrayList<String> takenChars = new ArrayList<>();
                String line;
                try {
                    FileReader fileReader = new FileReader(f);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);

                    while ((line = bufferedReader.readLine()) != null) {
                        if (line.contains("graphic:")) {
                            takenChars.add(line.substring(
                                    line.indexOf(":") + 1, line.length()));
                        }
                    }
                    bufferedReader.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("Error: file not found");
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error reading file");
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
     */

    public static void resetDefaults() {
        // reset keys
        MOVE_DOWN = DOWN_DEFAULT;
        MOVE_UP = UP_DEFAULT;
        MOVE_RIGHT = RIGHT_DEFAULT;
        MOVE_LEFT = LEFT_DEFAULT;

        SEARCH = SEARCH_DEFAULT;
        USE_STAIRCASE = USE_STAIRCASE_DEFUALT;
        INVENTORY = INVENTORY_DEFUALT;
        REST = REST_DEFAULT;

        //other resets to come
    }

}
