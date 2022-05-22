package entityimpl2.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles name obfuscation in various {@link entityimpl2.lifeless.item.structure.Item} classes.
 */
public class Obfuscator<T> {

    private final Map<String, T> decoderMap = new HashMap<>();
    private final List<String> names;
    private final Map<T, Boolean> isObfuscatedMap = new HashMap<>();

    public Obfuscator(List<String> names, T[] values) {
        this.names = names;
        for (T t : values) {
            this.isObfuscatedMap.put(t, false);
        }
    }

    public T read(String display) {
        return decoderMap.get(display);
    }

    public boolean shown(T t) {
        return isObfuscatedMap.get(t);
    }

    public void setShown(T t, boolean shown) {
        isObfuscatedMap.put(t, shown);
    }

    public List<String> getNames() {
        return names;
    }

    public void randomize() {
        Collections.shuffle(names);

        for (T t : isObfuscatedMap.keySet()) {
            for (String name : names) {
                decoderMap.put(name, t);
            }
        }
    }
}
