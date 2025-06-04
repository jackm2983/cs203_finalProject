import processing.core.PImage;

import java.util.List;

public class Obstacle extends ActionObject {
    public static final String OBSTACLE_KEY = "obstacle";
    public static final int OBSTACLE_ANIMATION_PERIOD_IDX = 0;
    public static final int OBSTACLE_NUM_PROPERTIES = 1;

    public Obstacle (String id, Point position, double animationPeriod, List<PImage> images) {
        super(id, position, images, animationPeriod, 0);
    }

    @Override
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this, new AnimationAction(this, 0), getAnimationPeriod());
    }
}
