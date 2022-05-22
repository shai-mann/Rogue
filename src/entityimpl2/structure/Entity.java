package entityimpl2.structure;

import rendering.Renderable;
import rendering.Renderer;

import java.awt.*;

/**
 * An Entity is a {@link Renderer} game object with a couple traits; a {@link Point} location,
 * a {@link String} name, a {@link String} graphic, and a display {@link Color} for that graphic.
 * @see LivingEntity
 */
public interface Entity extends Renderable {

    Point location();

    String name();

    String graphic();

    Color tileColor();

}
