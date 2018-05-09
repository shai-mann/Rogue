package entity;

import java.awt.event.KeyEvent;

public class KeyBinds {

    private enum keyBinds {
        MOVE_DOWN,
        MOVE_UP,
        MOVE_RIGHT,
        MOVE_LEFT,
        SEARCH,
        USE_STAIRCASE,
        INVENTORY
    }

    public static int MOVE_DOWN = KeyEvent.VK_S;
    public static int MOVE_DOWN_ALT = KeyEvent.VK_DOWN;

    public static int MOVE_UP = KeyEvent.VK_W;
    public static int MOVE_UP_ALT = KeyEvent.VK_UP;

    public static int MOVE_RIGHT = KeyEvent.VK_D;
    public static int MOVE_RIGHT_ALT = KeyEvent.VK_RIGHT;

    public static int MOVE_LEFT = KeyEvent.VK_A;
    public static int MOVE_LEFT_ALT = KeyEvent.VK_LEFT;

    public static int SEARCH = KeyEvent.VK_F;
    public static int USE_STAIRCASE = KeyEvent.VK_ENTER;
    public static int INVENTORY = KeyEvent.VK_I;

    public static int PAUSE = KeyEvent.VK_ESCAPE;

    private static int[] keyBinds = {
            MOVE_DOWN, MOVE_DOWN_ALT,
            MOVE_UP, MOVE_UP_ALT,
            MOVE_RIGHT, MOVE_RIGHT_ALT,
            MOVE_LEFT, MOVE_LEFT_ALT,
            SEARCH,
            USE_STAIRCASE,
            INVENTORY,
            PAUSE
    };

    public static boolean setKeyBind(keyBinds type, int keyBind) {
        for (int i : keyBinds) {
            if (i == keyBind) {
                return false;
            }
        }
        switch (type) {
            case MOVE_DOWN:
                MOVE_DOWN = keyBind;
                break;
            case MOVE_UP:
                MOVE_UP = keyBind;
                break;
            case MOVE_RIGHT:
                MOVE_RIGHT = keyBind;
                break;
            case MOVE_LEFT:
                MOVE_LEFT = keyBind;
                break;
            case SEARCH:
                SEARCH = keyBind;
                break;
            case USE_STAIRCASE:
                USE_STAIRCASE = keyBind;
                break;
            case INVENTORY:
                INVENTORY = keyBind;
                break;
            default:
                return false;
        }
        return true;
    }
}
