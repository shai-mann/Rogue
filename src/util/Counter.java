package util;

public class Counter {

    private int value = 0;

    public void tick() {
        if (isTicking()) value -= 1;
    }

    public void add(int value) {
        this.value += value;
    }

    public void reset() {
        this.value = 0;
    }

    public boolean isTicking() {
        return value > 0;
    }
}
