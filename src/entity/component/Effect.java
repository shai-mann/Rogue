package entity.component;

import java.io.Serializable;

public record Effect(Type type) implements Serializable {
    public enum Type {
        PROTECT_ARMOR,
        CONFUSE_ON_HIT,
        SUPPRESS_POWER,
        PROTECTION,
        SEARCHING,
        SEE_INVISIBLE,
        SUSTAIN_STRENGTH,
        STRENGTH,
        REGENERATION,
        AGGRAVATE_MONSTER,
        SLOW_DIGESTION
    }
}
