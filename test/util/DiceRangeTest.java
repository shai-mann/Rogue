package util;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class DiceRangeTest {

    private int getField(DiceRange diceRange, String field) {
        int out = 0;

        try {
            Field f = diceRange.getClass().getDeclaredField(field);
            f.setAccessible(true);
            out = f.getInt(diceRange);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getMessage());
        }

        return out;
    }

    private int getNumDice(DiceRange diceRange) {
        return getField(diceRange, "numDice");
    }

    private int getNumFaces(DiceRange diceRange) {
        return getField(diceRange, "numFaces");
    }

    @Test
    public void create() {
        DiceRange diceRange = new DiceRange("4d3");

        assertEquals(getNumDice(diceRange), 4);
        assertEquals(getNumFaces(diceRange), 3);
    }

    @Test
    public void scale() {
        DiceRange diceRange = new DiceRange("12d6").scale(3);

        assertEquals(diceRange.toString(), new DiceRange("36d6").toString());
    }

    @Test
    public void getValue() {
        int faces = 4;
        int dice = 6;
        DiceRange diceRange = new DiceRange(dice + "d" + faces);

        for (int i = 0; i < 100; i++) {
            int value = diceRange.getValue();
            System.out.println(value);
            assertTrue(value <= faces * dice);
            assertTrue(value >= dice);
        }
    }

    @Test
    public void testToString() {
        assertEquals("4d6", new DiceRange("4d6").toString());
    }
}