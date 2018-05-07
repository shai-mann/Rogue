package map.level;

import main.GameManager;

import java.awt.*;

public class Door extends Point {

    private boolean secret = false;
    private String overWrittenGraphic;

    public Door(Point p, boolean secret, String overWrittenGraphic) {
        super(p);
        this.secret = secret;
        this.overWrittenGraphic = overWrittenGraphic;
        if (isSecret()) {
            GameManager.getTable().setValueAt(overWrittenGraphic, p.y, p.x);
        }
        // TODO: Figure out why secret doors aren't working
    }

    // GETTER METHODS

    public boolean isSecret() {
        return secret;
    }
    public String getOverWrittenGraphic() {
        return overWrittenGraphic;
    }
}
