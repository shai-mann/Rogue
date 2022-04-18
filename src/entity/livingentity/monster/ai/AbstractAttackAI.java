package entity.livingentity.monster.ai;

import entity.livingentity.Player;
import entity.livingentity.monster.Monster;
import main.GameManager;
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
    }};

    public AbstractAttackAI(Monster self) {
        this.player = GameManager.getPlayer();
        this.self = self;
    }

    protected boolean isSuccessfulAttack() {
        return Helper.calculateChance(GameManager.getPlayer().getStatus().getAc() / 10.0);
    }

    protected boolean isCrit() {
        return Helper.calculateChance(self.attributes().critChance());
    }

    protected abstract String successMessage();
    protected String failMessage() {
        return "The " + self.getName() + " misses";
    }
    protected abstract String critMessage();

    protected abstract void performAttack(boolean isCrit);

    @Override
    public Outcome attack() {
        if (!canAttack()) {
            return Outcome.UNPERFORMED;
        }

        if (!isSuccessfulAttack()) {
            MessageBar.addMessage(messageMap.get(Outcome.FAIL).get());
            return Outcome.FAIL;
        }

        boolean isCrit = isCrit();
        Outcome outcome = isCrit ? Outcome.CRIT : Outcome.SUCCESS;

        performAttack(isCrit);
        MessageBar.addMessage(messageMap.get(outcome).get());

        return outcome;
    }

    public boolean canAttack() {
        return Helper.isNextTo(self, player);
    }

}
