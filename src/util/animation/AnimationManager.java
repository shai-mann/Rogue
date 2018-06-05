package util.animation;

import java.awt.*;
import java.util.ArrayList;

public class AnimationManager {

    private ArrayList<Animation> animations = new ArrayList<>();

    public AnimationManager() {

    }
    public void addAnimation(Animation a) {
        animations.add(a);
    }
    public void update() {
        ArrayList<Animation> queuedAnimations = new ArrayList<>();
        for (Animation a : animations) {
            if (a.getIterations() >= 5) {
                queuedAnimations.add(a);
            } else {
                a.update();
            }
        }
        animations.removeAll(queuedAnimations);
    }
    public Animation contains(Point p) {
        for (Animation a : animations) {
            if (a.contains(p)) {
                return a;
            }
        }
        return null;
    }
}
