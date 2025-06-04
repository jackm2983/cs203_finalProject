import processing.core.PImage;

import java.util.List;

public abstract class Dude extends ActionObject implements Moveable, Executable, Transformable {
    public static final String DUDE_KEY = "dude";
    public static final int DUDE_ACTION_PERIOD_IDX = 0;
    public static final int DUDE_ANIMATION_PERIOD_IDX = 1;
    public static final int DUDE_RESOURCE_LIMIT_IDX = 2;
    public static final int DUDE_NUM_PROPERTIES = 3;
    private final int resourceLimit;
    private int resourceCount;

    public Dude (String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, double actionPeriod, double animationPeriod) {
        super(id, position, images, animationPeriod, actionPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    public int getResourceLimit() {return this.resourceLimit;};

    public int getResourceCount() {return this.resourceCount;}

    public void setResourceCount(int count) {
        this.resourceCount = count;
    }
}
