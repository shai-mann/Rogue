package entityimpl2.component;

import util.Counter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Status implements Serializable {

    public enum Stat {
        PARALYZED,
        CONFUSED,
        DRUNK,
        WEAKENED,
        SLEEPING,
        POISONED,
        STRENGTHENED,
        BLINDED
    }

    private int ac;
    private final List<Effect> effects = new ArrayList<>();
    private final Map<Stat, Counter> counters = new HashMap<>() {{
        put(Stat.PARALYZED, new Counter());
        put(Stat.CONFUSED, new Counter());
        put(Stat.DRUNK, new Counter());
        put(Stat.WEAKENED, new Counter());
        put(Stat.SLEEPING, new Counter());
        put(Stat.POISONED, new Counter());
        put(Stat.STRENGTHENED, new Counter());
        put(Stat.BLINDED, new Counter());
    }};

    public Status(int ac) {
        this.ac = ac;
    }

    /* EFFECTS */

    public boolean hasEffect(Effect.Type type) {
        return effects.stream().anyMatch(effect -> effect.type().equals(type));
    }

    /* UPDATE */

    public void update() {
        for (Counter c : counters.values()) {
            c.tick();
        }
    }

    public int getAc() {
        if (hasEffect(Effect.Type.PROTECTION)) {
            return ac + 1;
        } else {
            return ac;
        }
    }

    public boolean is(Stat stat) {
        return counters.get(stat).isTicking();
    }

    public void add(Stat stat, int turns) {
        counters.get(stat).add(turns);
    }

    public void reset(Stat stat) {
        counters.get(stat).reset();
    }

    public void add(Effect effect) {
        this.effects.add(effect);
    }

    public void remove(Effect effect) {
        this.effects.remove(effect);
    }

    public void setAc(int ac) {
        this.ac = Math.max(ac, 3);
    }

}
