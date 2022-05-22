package entityimpl2.monster.ai;

import entityimpl2.monster.Monster;
import entityimpl2.player.Player;
import map.level.Level;
import util.Helper;
import util.gamepanes.MessageBar;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractAttackAI implements AttackAI {

    protected final Player player;
    protected final Monster self;

    /**
     * The use of {@link Supplier}s allows the messages to be updated based on changing values
     */
    private final Map<Outcome, Supplier<String>> messageMap = new HashMap<>() {{
        put(Outcome.SUCCESS, () -> successMessage());
        put(Outcome.FAIL, () -> failMessage());
        put(Outcome.CRIT, () -> critMessage());
        put(Outcome.BLOCKED, () -> blockedMessage());
    }};

    public AbstractAttackAI(Monster self) {
        this.player = Level.getLevel().getPlayer();
        this.self = self;
    }

    private boolean isSuccessfulAttack() {
        return Helper.calculateChance(player.getStatus().getAc() / 10.0);
    }

    private boolean isCrit() {
        return Helper.calculateChance(self.attributes().critChance());
    }

    protected boolean isCrit(Outcome outcome) {
        return outcome.equals(Outcome.CRIT);
    }

    protected abstract String successMessage();
    protected String failMessage() {
        return "The " + self.name() + " misses";
    }
    protected String critMessage() {
        return successMessage();
    }
    protected String blockedMessage() {
        return failMessage();
    }

    /**
     * Attempts to perform the attack, which can change the {@link Outcome}.
     * The resulting outcome is returned.
     *
     * <p>An example of the outcome being changed during this method would be the weaken attack. If
     * the player is immune to weakness attacks, the outcome should mutate to a FAIL.</p>
     * @param outcome the current outcome (either CRIT or SUCCESS).
     * @return the adjusted outcome after performing the attack.
     */
    protected abstract Outcome performAttack(Outcome outcome);

    @Override
    public Outcome attack() {
        if (!canAttack()) {
            return Outcome.UNPERFORMED;
        }

        if (!isSuccessfulAttack()) {
            MessageBar.addMessage(messageMap.get(Outcome.FAIL).get());
            return Outcome.FAIL;
        }

        Outcome outcome = isCrit() ? Outcome.CRIT : Outcome.SUCCESS;
        outcome = performAttack(outcome);
        if (outcome != Outcome.UNPERFORMED) MessageBar.addMessage(messageMap.get(outcome).get());

        return outcome;
    }

    public boolean canAttack() {
        return Helper.isNextTo(self, player);
    }

}
