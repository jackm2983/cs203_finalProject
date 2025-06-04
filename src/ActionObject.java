import processing.core.PImage;

import java.util.List;

public abstract class ActionObject extends Entity {

    private final double animationPeriod;
    private final double actionPeriod;

    public ActionObject(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod) {
        super(id, position, images);
        this.animationPeriod = animationPeriod;
        this.actionPeriod = actionPeriod;
    }

    public double getAnimationPeriod() {
        return animationPeriod;
    }

    public double getActionPeriod() {
        return actionPeriod;
    }
    public abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);

}
