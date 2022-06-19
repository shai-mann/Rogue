package util;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Random;

import static org.junit.Assert.*;

public class CounterTest {

    private int generateValue() {
        return new Random().nextInt(10) + 1;
    }

    private Counter generate(int value) {
        Counter counter = new Counter();
        counter.add(value);

        return counter;
    }

    // using reflection, grabs the private 'value' field.
    private int getValue(Counter counter) {
        int out = 0;

        try {
            Field field = counter.getClass().getDeclaredField("value");
            field.setAccessible(true);
            out = field.getInt(counter);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getMessage());
        }

        return out;
    }

    @Test
    public void tick() {
        int value = generateValue();
        Counter counter = generate(value);

        counter.tick();
        assertEquals(getValue(counter), value - 1);
    }

    @Test
    public void tickAtZero() {
        Counter counter = generate(0);
        counter.reset();

        counter.tick();
        assertEquals(getValue(counter), 0); // doesn't go below 0
    }

    @Test
    public void add() {
        Counter counter = new Counter();

        assertEquals(getValue(counter), 0);

        int value = generateValue();
        counter.add(value);
        assertEquals(getValue(counter), value);

        int value2 = generateValue();
        counter.add(value2);
        assertEquals(getValue(counter), value + value2);
    }

    @Test
    public void reset() {
        int value = generateValue();
        Counter counter = generate(value);

        assertNotEquals(getValue(counter), 0);
        counter.reset();
        assertEquals(getValue(counter), 0);
    }

    @Test
    public void isTickingWhenNotZeroIsTrue() {
        Counter counter = generate(generateValue());
        assertTrue(counter.isTicking());
    }

    @Test
    public void isTickingWhenAtZeroIsFalse() {
        Counter counter = generate(0);
        assertFalse(counter.isTicking());
    }
}